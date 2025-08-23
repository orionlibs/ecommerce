package de.hybris.platform.task.impl.gateways;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.impl.TasksProvider;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class DefaultTasksQueueGateway extends DefaultBaseGateway implements TasksQueueGateway
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTasksQueueGateway.class);
    private static final String TABLE_NAME = "tasks_aux_queue";
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT NOW() NOT NULL, EXECUTION_TIME BIGINT)";
    private static final String INSERT_TASK_ROW = "INSERT INTO {0} (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) AS A WHERE NOT EXISTS (SELECT 1 FROM {0} AS B WHERE A.PK = B.PK)";
    protected static final String SELECT_TASKS_WITH_RANGE = "SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND t.NODE_GROUP = ''{2}'') OR (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit";
    protected static final String SELECT_TASKS_EXCLUSIVE_MODE = "SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit";
    private static final String DELETE_TASKS_FROM_QUEUE = "DELETE FROM {0} WHERE PK IN (:pks)";
    private static final String CLEAN_TASKS_QUEUE = "DELETE FROM {0} WHERE PROCESSING_TIME < NOW() - INTERVAL {2,number,#} SECOND AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)";
    private static final String SELECT_TASKS_COUNT = "SELECT NODE_ID, NODE_GROUP, COUNT(*) from {0} group by node_id, NODE_GROUP";
    private static final String LOCK_TASKS_FOR_PROCESSING = "UPDATE {0} SET PROCESSING_TIME = now() + interval {1} second WHERE PK = ? AND PROCESSING_TIME = ?";
    private static final String UNLOCK_TASKS = "UPDATE {0} SET {1} = -1 WHERE {1} IN (:ids)";
    public static final String EMPTY_GROUP_VALUE = "---";
    public static final String PROPERTY_RETRIES_IF_DEADLOCK_ON_GET_TASKS = "task.auxiliaryTables.tasks.get.retriesIfDeadlock";
    private static final JDBCValueMappings jdbcValueMappings = JDBCValueMappings.getInstance();
    private final JDBCValueMappings.ValueWriter<PK, ?> pkWriter = jdbcValueMappings.PK_WRITER;
    private final JDBCValueMappings.ValueReader<PK, ?> pkReader = jdbcValueMappings.PK_READER;
    private final JDBCValueMappings.ValueReader<Integer, ?> intReader = jdbcValueMappings.getValueReader(Integer.class);
    protected final JDBCValueMappings.ValueWriter<Integer, ?> intWriter = jdbcValueMappings.getValueWriter(Integer.class);
    private final JDBCValueMappings.ValueReader<Long, ?> longReader = jdbcValueMappings.getValueReader(Long.class);
    protected final JDBCValueMappings.ValueWriter<Long, ?> longWriter = jdbcValueMappings.getValueWriter(Long.class);
    private final JDBCValueMappings.ValueReader<Date, ?> dateReader = jdbcValueMappings.getValueReader(Date.class);
    protected final JDBCValueMappings.ValueWriter<Date, ?> dateWriter = jdbcValueMappings.getValueWriter(Date.class);
    private final TypeService typeService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public DefaultTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate);
        this.typeService = typeService;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate((JdbcOperations)jdbcTemplate);
    }


    public String getTableName()
    {
        return getTenantAwareTableName("tasks_aux_queue");
    }


    protected String getTableName(String code)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(code);
        return type.getTable();
    }


    private String getColumnName(String code, String column)
    {
        ComposedTypeModel type = this.typeService.getComposedTypeForCode(code);
        return ((AttributeDescriptorModel)type.getDeclaredattributedescriptors().stream().filter(s -> s.getQualifier().equalsIgnoreCase(column)).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("type " + code + " is not recognizable"))).getDatabaseColumn();
    }


    public List<TasksProvider.VersionPK> getTasksForWorkerAndMarkForProcessing(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Duration lockDuration)
    {
        int retries = Config.getInt("task.auxiliaryTables.tasks.get.retriesIfDeadlock", 0);
        while(true)
        {
            try
            {
                return (List<TasksProvider.VersionPK>)Transaction.current()
                                .execute(() -> getTasksForWorkerAndMarkForProcessingInternal(range, maxItemsToSchedule, workerState, lockDuration));
            }
            catch(DeadlockLoserDataAccessException e)
            {
                LOGGER.warn("DB deadlock occurred while trying to get tasks from {}. Attempts left: {}. {}. To see stacktrace, enable DEBUG level of this logger", new Object[] {getTableName(), Integer.valueOf(retries), e.getMessage()});
                LOGGER.debug(e.getMessage(), (Throwable)e);
                if(retries <= 0)
                {
                    throw new IllegalStateException(e);
                }
                retries--;
            }
            catch(Exception e)
            {
                throw new IllegalStateException(e);
            }
        }
    }


    protected List<TasksProvider.VersionPK> getTasksForWorkerAndMarkForProcessingInternal(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Duration lockDuration)
    {
        List<TasksQueueRow> tasks = getTasksInternal(range, maxItemsToSchedule, workerState, lockDuration);
        if(CollectionUtils.isEmpty(tasks))
        {
            return Collections.emptyList();
        }
        getLockTasksForProcessingStatementIfNeeded(lockDuration).ifPresent(s -> markTasksForProcessingInternal(s, tasks));
        return (List<TasksProvider.VersionPK>)tasks.stream().map(TasksQueueRow::getVersionPK).collect(Collectors.toList());
    }


    private List<TasksQueueRow> getTasksInternal(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Duration lockDuration)
    {
        List<TasksQueueRow> tasks;
        Set<String> nodeGroups = CollectionUtils.isEmpty(workerState.getNodeGroups()) ? Collections.<String>singleton("") : workerState.getNodeGroups();
        if(workerState.isExclusiveMode())
        {
            String query = getSelectTasksWithExclusiveModeQuery();
            Map<String, Object> selectTasksWithExclusiveModeParams = getSelectTasksWithExclusiveModeParams(maxItemsToSchedule, workerState, nodeGroups, lockDuration);
            tasks = this.namedParameterJdbcTemplate.query(query, selectTasksWithExclusiveModeParams, getTasksQueueRowRowMapper());
        }
        else
        {
            String query = getSelectTasksWithRangeQuery();
            Map<String, Object> selectTasksWithRangeParams = getSelectTasksWithRangeParams(range, maxItemsToSchedule, workerState, nodeGroups, lockDuration);
            tasks = this.namedParameterJdbcTemplate.query(query, selectTasksWithRangeParams, getTasksQueueRowRowMapper());
        }
        return tasks;
    }


    protected Map<String, Object> getSelectTasksWithRangeParams(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Set<String> groups, Duration lockDuration)
    {
        return (Map<String, Object>)ImmutableMap.builder().put("nodeId", Integer.valueOf(workerState.getNodeId()))
                        .put("nodeGroups", groups)
                        .put("limit", Long.valueOf(maxItemsToSchedule))
                        .put("rangeStart", Long.valueOf(range.getStart()))
                        .put("rangeEnd", Long.valueOf(range.getEnd()))
                        .build();
    }


    protected Map<String, Object> getSelectTasksWithExclusiveModeParams(long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Set<String> groups, Duration lockDuration)
    {
        return (Map<String, Object>)ImmutableMap.builder().put("nodeId", Integer.valueOf(workerState.getNodeId()))
                        .put("nodeGroups", groups)
                        .put("limit", Long.valueOf(maxItemsToSchedule))
                        .build();
    }


    protected String getSelectTasksWithRangeQuery()
    {
        return MessageFormatUtils.format(
                        "SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND t.NODE_GROUP = ''{2}'') OR (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit",
                        new Object[] {getTableName(), getTableName("Task"),
                                        getEmptyGroupValue()}) + " FOR UPDATE";
    }


    protected String getSelectTasksWithExclusiveModeQuery()
    {
        return MessageFormatUtils.format("SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit",
                        new Object[] {getTableName(), getTableName("Task"),
                                        getEmptyGroupValue()}) + " FOR UPDATE";
    }


    private RowMapper<TasksProvider.VersionPK> getVersionPKRowMapper()
    {
        return (resultSet, i) -> {
            PK taskPk = (PK)this.pkReader.getValue(resultSet, 1);
            Long hjmpTS = (Long)this.longReader.getValue(resultSet, 2);
            return new TasksProvider.VersionPK(taskPk, hjmpTS.longValue());
        };
    }


    private RowMapper<TasksQueueRow> getTasksQueueRowRowMapper()
    {
        return (resultSet, i) -> {
            PK taskPk = (PK)this.pkReader.getValue(resultSet, 1);
            Long hjmpTS = (Long)this.longReader.getValue(resultSet, 2);
            Date processingTime = (Date)this.dateReader.getValue(resultSet, 3);
            return new TasksQueueRow(new TasksProvider.VersionPK(taskPk, hjmpTS.longValue()), processingTime);
        };
    }


    public void clean(Duration taskProcessingTimeThreshold)
    {
        try
        {
            Transaction.current().execute(() -> {
                cleanInternal(taskProcessingTimeThreshold);
                return null;
            });
        }
        catch(BadSqlGrammarException exception)
        {
            if(!tryCreatingTableAndLogException(LOGGER, "Error while cleaning tasks' queue", (Exception)exception))
            {
                throw exception;
            }
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
    }


    protected void cleanInternal(Duration taskProcessingTimeThreshold)
    {
        String query = getCleanTasksQueueStatement(taskProcessingTimeThreshold);
        this.jdbcTemplate.update(query);
    }


    protected String getCleanTasksQueueStatement(Duration taskProcessingTimeThreshold)
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE PROCESSING_TIME < NOW() - INTERVAL {2,number,#} SECOND AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)", new Object[] {getTableName(), getTableName("Task"),
                        Long.valueOf(taskProcessingTimeThreshold.getSeconds())});
    }


    public long addTasks(String tasksQuery, String expiredTasksQuery, Instant now, int rangeStart, int rangeEnd)
    {
        try
        {
            return ((Long)Transaction.current().execute(() -> Long.valueOf(addTasksInternal(tasksQuery, expiredTasksQuery, now, rangeStart, rangeEnd)))).longValue();
        }
        catch(Exception e)
        {
            LOGGER.error("Error while marking tasks to process", e);
            throw new IllegalStateException(e);
        }
    }


    protected long addTasksInternal(String tasksQuery, String expiredTasksQuery, Instant now, int rangeStart, int rangeEnd)
    {
        long count = 0L;
        try
        {
            String random = getRangeSQLExpression(rangeStart, rangeEnd);
            String statement = getInsertTaskRowStatement(MessageFormatUtils.format(tasksQuery, new Object[] {random}));
            count += this.jdbcTemplate.update(statement, new Object[] {Long.valueOf(now.toEpochMilli())});
            count += this.jdbcTemplate.update(getInsertTaskRowStatement(MessageFormatUtils.format(expiredTasksQuery, new Object[] {random})), new Object[] {Long.valueOf(now.toEpochMilli())});
        }
        catch(Exception ex)
        {
            LOGGER.error("error while adding tasks to queue", ex);
            throw ex;
        }
        return count;
    }


    protected String getInsertTaskRowStatement(String subquery)
    {
        return MessageFormatUtils.format("INSERT INTO {0} (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) AS A WHERE NOT EXISTS (SELECT 1 FROM {0} AS B WHERE A.PK = B.PK)", new Object[] {getTableName(), subquery});
    }


    public String defaultIfNull(String columnName, Integer defaultValue)
    {
        Objects.requireNonNull(columnName);
        Objects.requireNonNull(defaultValue);
        return MessageFormatUtils.format("CASE WHEN {0} IS NULL THEN {1,number,#} ELSE {0} END", new Object[] {columnName, defaultValue});
    }


    public String defaultIfNull(String columnName, String defaultValue)
    {
        Objects.requireNonNull(columnName);
        Objects.requireNonNull(defaultValue);
        return MessageFormatUtils.format("CASE WHEN {0} IS NULL THEN ''''{1}'''' ELSE {0} END", new Object[] {columnName, defaultValue});
    }


    public String getEmptyGroupValue()
    {
        return "---";
    }


    public String getRangeSQLExpression(int rangeStart, int rangeEnd)
    {
        return MessageFormatUtils.format("floor({0,number,#} + (rand()*({1,number,#}-{0,number,#})))", new Object[] {Integer.valueOf(rangeStart), Integer.valueOf(rangeEnd)});
    }


    public List<TasksQueueGateway.TasksCountResult> getTasksCount()
    {
        try
        {
            String query = MessageFormatUtils.format("SELECT NODE_ID, NODE_GROUP, COUNT(*) from {0} group by node_id, NODE_GROUP", new Object[] {getTableName()});
            return this.jdbcTemplate.query(query, (resultSet, i) -> {
                Integer nodeId = (Integer)this.intReader.getValue(resultSet, 1);
                String nodeGroup = resultSet.getString(2);
                Long count = (Long)this.longReader.getValue(resultSet, 3);
                return new TasksQueueGateway.TasksCountResult(nodeId, nodeGroup, count.longValue());
            });
        }
        catch(BadSqlGrammarException exception)
        {
            LOGGER.warn("error while getting tasks count: {}", exception.getMessage());
            LOGGER.debug("error while getting tasks count", (Throwable)exception);
            throw exception;
        }
    }


    public boolean createTable()
    {
        try
        {
            this.jdbcTemplate.execute(getCreateTableStatement());
            List<String> indexes = getCreateTableIndexStatements();
            Objects.requireNonNull(this.jdbcTemplate);
            indexes.forEach(this.jdbcTemplate::execute);
            return true;
        }
        catch(DataAccessException ex)
        {
            LOGGER.warn("error while creating table {}", getTableName(), ex);
            return false;
        }
    }


    protected List<String> getCreateTableIndexStatements()
    {
        return
                        Lists.newArrayList((Object[])new String[] {MessageFormatUtils.format("CREATE INDEX {0}_idx1 ON {0} (PROCESSING_TIME ASC )", new Object[] {getTableName()}),
                                        MessageFormatUtils.format("CREATE INDEX {0}_idx2 ON {0} (EXECUTION_TIME ASC, RANGE_VALUE ASC, PROCESSING_TIME ASC )", new Object[] {getTableName()}),
                                        MessageFormatUtils.format("CREATE INDEX {0}_idx3 ON {0} (NODE_ID, NODE_GROUP)", new Object[] {getTableName()})});
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT NOW() NOT NULL, EXECUTION_TIME BIGINT)",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected void markTasksForProcessingInternal(String lockStatement, List<TasksQueueRow> tasksForWorker)
    {
        this.jdbcTemplate.batchUpdate(lockStatement, tasksForWorker, tasksForWorker.size(), (preparedStatement, tasksQueueRow) -> {
            this.pkWriter.setValue(preparedStatement, 1, PK.fromLong(tasksQueueRow.getVersionPK().getLongValue().longValue()));
            this.dateWriter.setValue(preparedStatement, 2, tasksQueueRow.getProcessingTime());
        });
    }


    @Deprecated(since = "20.05", forRemoval = true)
    protected String getLockTasksForProcessingStatement(Duration lockDuration)
    {
        return MessageFormatUtils.format("UPDATE {0} SET PROCESSING_TIME = now() + interval {1} second WHERE PK = ? AND PROCESSING_TIME = ?", new Object[] {getTableName(), Long.valueOf(lockDuration.getSeconds())});
    }


    protected Optional<String> getLockTasksForProcessingStatementIfNeeded(Duration lockDuration)
    {
        return Optional.ofNullable(getLockTasksForProcessingStatement(lockDuration));
    }


    public String getUnlockTasksStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET {1} = -1 WHERE {1} IN (:ids)", new Object[] {getTableName("Task"),
                        getColumnName("Task", "runningOnClusterNode")});
    }


    public void deleteTasks(List<PK> tasks)
    {
        if(tasks == null || tasks.isEmpty())
        {
            return;
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("pks", tasks.stream().map(PK::getLong).collect(Collectors.toList()));
        String sql = getDeleteTasksStatement();
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, sql, parameters));
        }
        catch(Exception e)
        {
            LOGGER.error("error while deleting tasks", e);
            throw new IllegalStateException(e);
        }
    }


    protected String getDeleteTasksStatement()
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE PK IN (:pks)", new Object[] {getTableName()});
    }


    public void unlockTasksForWorkers(List<Integer> invalidWorkerIds)
    {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", invalidWorkerIds);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
        template.update(getUnlockTasksStatement(), (SqlParameterSource)parameters);
    }


    public List<TasksProvider.VersionPK> getConditionsToSchedule(String conditionsQuery, Instant time)
    {
        return this.jdbcTemplate.query(conditionsQuery, getVersionPKRowMapper(), new Object[] {Long.valueOf(time.toEpochMilli())});
    }
}
