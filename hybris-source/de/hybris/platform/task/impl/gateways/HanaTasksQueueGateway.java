package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.impl.TasksProvider;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class HanaTasksQueueGateway extends DefaultTasksQueueGateway
{
    public static final String PROPERTY_DISABLE_LOCK_ON_GET_TASKS = "task.auxiliaryTables.tasks.get.tableLock.disabled";
    private static final int HANA_1_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE BIGINT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME SECONDDATE DEFAULT CURRENT_TIMESTAMP NOT NULL, EXECUTION_TIME BIGINT)";
    private static final String CLEAN_TASKS_QUEUE = "DELETE FROM {0} WHERE PROCESSING_TIME < ADD_SECONDS(NOW(),-{2,number,#}) AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)";
    private static final String LOCK_TASKS_FOR_PROCESSING = "UPDATE {0} SET PROCESSING_TIME = ADD_SECONDS(now(), {1, number, #}) WHERE PK = ? AND PROCESSING_TIME = ?";
    private static final Logger LOGGER = LoggerFactory.getLogger(HanaTasksQueueGateway.class);
    private final Supplier<Optional<AdditionalDatabaseData>> additionalDatabaseDataSupplier;


    @Deprecated(since = "2211", forRemoval = true)
    public HanaTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        this(jdbcTemplate, typeService, Optional::empty);
    }


    public HanaTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService, Supplier<Optional<AdditionalDatabaseData>> additionalDatabaseDataSupplier)
    {
        super(jdbcTemplate, typeService);
        this.additionalDatabaseDataSupplier = additionalDatabaseDataSupplier;
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE BIGINT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME SECONDDATE DEFAULT CURRENT_TIMESTAMP NOT NULL, EXECUTION_TIME BIGINT)",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected String getCleanTasksQueueStatement(Duration taskProcessingTimeThreshold)
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE PROCESSING_TIME < ADD_SECONDS(NOW(),-{2,number,#}) AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)", new Object[] {getTableName(), getTableName("Task"),
                        Long.valueOf(taskProcessingTimeThreshold.getSeconds())});
    }


    protected String getSelectTasksWithRangeQuery()
    {
        return MessageFormatUtils.format(
                        "SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND t.NODE_GROUP = ''{2}'') OR (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit FOR UPDATE OF PROCESSING_TIME",
                        new Object[] {getTableName(),
                                        getTableName("Task"),
                                        getEmptyGroupValue()});
    }


    protected String getSelectTasksWithExclusiveModeQuery()
    {
        return MessageFormatUtils.format(
                        "SELECT t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= NOW() ORDER BY EXECUTION_TIME LIMIT :limit FOR UPDATE OF PROCESSING_TIME",
                        new Object[] {getTableName(),
                                        getTableName("Task"),
                                        getEmptyGroupValue()});
    }


    protected void cleanInternal(Duration taskProcessingTimeThreshold)
    {
        lockTable();
        super.cleanInternal(taskProcessingTimeThreshold);
    }


    protected long addTasksInternal(String tasksQuery, String expiredTasksQuery, Instant now, int rangeStart, int rangeEnd)
    {
        lockTable();
        return super.addTasksInternal(tasksQuery, expiredTasksQuery, now, rangeStart, rangeEnd);
    }


    protected List<TasksProvider.VersionPK> getTasksForWorkerAndMarkForProcessingInternal(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Duration lockDuration)
    {
        if(isLockTableForGetTasksRequired(workerState))
        {
            lockTable();
        }
        return super.getTasksForWorkerAndMarkForProcessingInternal(range, maxItemsToSchedule, workerState, lockDuration);
    }


    private boolean isLockTableForGetTasksRequired(WorkerStateGateway.WorkerState workerState)
    {
        return (!workerState.getNodeGroups().isEmpty() && !Config.getBoolean("task.auxiliaryTables.tasks.get.tableLock.disabled", false) && isHana1Used());
    }


    private boolean isHana1Used()
    {
        return (((Integer)((Optional)this.additionalDatabaseDataSupplier.get()).map(AdditionalDatabaseData::getMajorDbVersion).orElse(Integer.valueOf(0))).intValue() == 1);
    }


    private String getLockTableStatement()
    {
        return MessageFormatUtils.format("LOCK TABLE {0} IN EXCLUSIVE MODE", new Object[] {getTableName()});
    }


    void lockTable()
    {
        LOGGER.debug("locking table");
        this.jdbcTemplate.execute(getLockTableStatement());
    }


    protected Optional<String> getLockTasksForProcessingStatementIfNeeded(Duration lockDuration)
    {
        return Optional.of(MessageFormatUtils.format("UPDATE {0} SET PROCESSING_TIME = ADD_SECONDS(now(), {1, number, #}) WHERE PK = ? AND PROCESSING_TIME = ?", new Object[] {getTableName(), Long.valueOf(lockDuration.getSeconds())}));
    }
}
