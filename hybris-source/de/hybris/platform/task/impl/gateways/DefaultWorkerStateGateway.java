package de.hybris.platform.task.impl.gateways;

import com.google.common.collect.Sets;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class DefaultWorkerStateGateway extends DefaultBaseGateway implements WorkerStateGateway
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWorkerStateGateway.class);
    private static final String TABLE_NAME = "tasks_aux_workers";
    private static final String CREATE_TABLE = "CREATE TABLE {0} (ID BIGINT, RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)";
    private static final String INSERT_WORKER_ROW = "INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(NOW(), ?, ?, ?)";
    private static final String SELECT_WORKERS_HEALTH_CHECK = "SELECT ID, HEALTH_CHECK, NOW() FROM {0}";
    private static final String SELECT_WORKERS = "SELECT ID, HEALTH_CHECK, NOW(), EXCLUSIVE_MODE, NODE_GROUPS FROM {0}";
    private static final String SELECT_WORKER_RANGE = "SELECT RANGES FROM {0} WHERE ID=? AND IS_ACTIVE=1";
    private static final String UPDATE_WORKER_HEALTH_CHECK = "UPDATE {0} SET HEALTH_CHECK = NOW(), EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?";
    private static final String UPDATE_WORKER_RANGE = "UPDATE {0} SET RANGES = ?, IS_ACTIVE = 1 WHERE ID = ?";
    private static final String DELETE_WORKER = "DELETE FROM {0} WHERE ID = ? AND IS_ACTIVE = 0";
    private static final String DEACTIVATE_WORKER = "UPDATE {0} SET IS_ACTIVE = 0 WHERE ID = ?";
    private static final char NODE_GROUPS_SEPARATOR = ';';
    private static final JDBCValueMappings.ValueWriter<Integer, ?> intWriter = JDBCValueMappings.getJDBCValueWriter(Integer.class);
    private static final JDBCValueMappings.ValueReader<Integer, ?> intReader = JDBCValueMappings.getJDBCValueReader(Integer.class);
    private static final JDBCValueMappings.ValueWriter<String, ?> stringWriter = JDBCValueMappings.getJDBCValueWriter(String.class);
    private static final JDBCValueMappings.ValueReader<String, ?> stringReader = JDBCValueMappings.getJDBCValueReader(String.class);
    private static final JDBCValueMappings.ValueReader<Date, ?> dateReader = JDBCValueMappings.getJDBCValueReader(Date.class);


    public DefaultWorkerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    public void registerAsWorker(WorkerStateGateway.WorkerState workerState)
    {
        try
        {
            internalRegisterAsWorker(workerState);
        }
        catch(BadSqlGrammarException exception)
        {
            if(tryCreatingTableAndLogException(LOGGER, "error while registering worker in table", (Exception)exception))
            {
                internalRegisterAsWorker(workerState);
            }
        }
    }


    private void internalRegisterAsWorker(WorkerStateGateway.WorkerState workerState)
    {
        Integer nodeId = Integer.valueOf(workerState.getNodeId());
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            intWriter.setValue(preparedStatement, 1, Integer.valueOf(workerState.isExclusiveMode() ? 1 : 0));
            stringWriter.setValue(preparedStatement, 2, StringUtils.join(workerState.getNodeGroups(), ';'));
            intWriter.setValue(preparedStatement, 3, nodeId);
        };
        String statement = getUpdateWorkerHealthCheckStatement();
        int updatedRowsCount = 0;
        try
        {
            updatedRowsCount = this.jdbcTemplate.update(statement, preparedStatementSetter);
        }
        catch(BadSqlGrammarException e)
        {
            tryCreatingTableAndLogException(LOGGER, "error while registering worker", (Exception)e);
        }
        if(updatedRowsCount == 0)
        {
            this.jdbcTemplate.update(getInsertWorkerRowStatement(), preparedStatementSetter);
        }
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID BIGINT, RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)", new Object[] {getTableName()});
    }


    protected String getInsertWorkerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(NOW(), ?, ?, ?)", new Object[] {getTableName()});
    }


    protected String getUpdateWorkerHealthCheckStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET HEALTH_CHECK = NOW(), EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?", new Object[] {getTableName()});
    }


    public String getTableName()
    {
        return getTenantAwareTableName("tasks_aux_workers");
    }


    public Map<Integer, Duration> getWorkersHealthChecks()
    {
        return (Map<Integer, Duration>)this.jdbcTemplate.query(getWorkersHealthChecksQuery(), (resultSet, i) -> {
            Instant workerHealthCheckTime = ((Date)dateReader.getValue(resultSet, 2)).toInstant();
            Instant dbNow = ((Date)dateReader.getValue(resultSet, 3)).toInstant();
            Duration between = Duration.between(workerHealthCheckTime, dbNow);
            Integer nodeId = (Integer)intReader.getValue(resultSet, 1);
            return Pair.of(nodeId, between);
        }).stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }


    protected String getWorkersHealthChecksQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, NOW() FROM {0}", new Object[] {getTableName()});
    }


    public Optional<List<WorkerStateGateway.WorkerRange>> getWorkerRangeById(int nodeID)
    {
        try
        {
            String query = getActiveWorkerRangeByIdQuery();
            return (Optional<List<WorkerStateGateway.WorkerRange>>)this.jdbcTemplate.queryForObject(query, (resultSet, i) -> {
                String value = (String)stringReader.getValue(resultSet, 1);
                return Optional.of(WorkerStateGateway.WorkerRange.fromString(value));
            } new Object[] {Integer.valueOf(nodeID)});
        }
        catch(EmptyResultDataAccessException ex)
        {
            LOGGER.debug("no results found", (Throwable)ex);
            return Optional.empty();
        }
    }


    protected String getActiveWorkerRangeByIdQuery()
    {
        return MessageFormatUtils.format("SELECT RANGES FROM {0} WHERE ID=? AND IS_ACTIVE=1", new Object[] {getTableName()});
    }


    public List<WorkerStateGateway.WorkerState> getWorkers()
    {
        String query = getSelectWorkersQuery();
        return this.jdbcTemplate.query(query, (resultSet, i) -> {
            Integer nodeId = (Integer)intReader.getValue(resultSet, 1);
            Instant healthCheck = ((Date)dateReader.getValue(resultSet, 2)).toInstant();
            Instant dbNow = ((Date)dateReader.getValue(resultSet, 3)).toInstant();
            boolean exclusiveMode = (((Integer)intReader.getValue(resultSet, 4)).intValue() != 0);
            String[] nodeGroups = StringUtils.split((String)stringReader.getValue(resultSet, 5), ';');
            return new WorkerStateGateway.WorkerState(nodeId.intValue(), Duration.between(healthCheck, dbNow), exclusiveMode, (nodeGroups != null) ? Sets.newHashSet((Object[])nodeGroups) : Collections.emptySet());
        });
    }


    protected String getSelectWorkersQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, NOW(), EXCLUSIVE_MODE, NODE_GROUPS FROM {0}", new Object[] {getTableName()});
    }


    public void updateWorkersRanges(Map<Integer, WorkerStateGateway.WorkerRange> ranges)
    {
        String query = MessageFormatUtils.format("UPDATE {0} SET RANGES = ?, IS_ACTIVE = 1 WHERE ID = ?", new Object[] {getTableName()});
        this.jdbcTemplate.batchUpdate(query, ranges.entrySet(), 20, (preparedStatement, worker) -> {
            stringWriter.setValue(preparedStatement, 1, ((WorkerStateGateway.WorkerRange)worker.getValue()).asString());
            intWriter.setValue(preparedStatement, 2, worker.getKey());
        });
    }


    public void deleteWorkers(List<Integer> invalidWorkerIds)
    {
        try
        {
            String query = MessageFormatUtils.format("DELETE FROM {0} WHERE ID = ? AND IS_ACTIVE = 0", new Object[] {getTableName()});
            this.jdbcTemplate.batchUpdate(query, invalidWorkerIds, 20, (preparedStatement, nodeId) -> intWriter.setValue(preparedStatement, 1, nodeId));
        }
        catch(BadSqlGrammarException ex)
        {
            if(!tryCreatingTableAndLogException(LOGGER, "could not unregister worker, because there was an error", (Exception)ex))
            {
                throw ex;
            }
        }
    }


    public void deactivateWorkers(List<Integer> invalidWorkerIds)
    {
        String query = MessageFormatUtils.format("UPDATE {0} SET IS_ACTIVE = 0 WHERE ID = ?", new Object[] {getTableName()});
        this.jdbcTemplate.batchUpdate(query, invalidWorkerIds, 20, (preparedStatement, nodeId) -> intWriter.setValue(preparedStatement, 1, nodeId));
    }
}
