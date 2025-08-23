package de.hybris.platform.task.impl.gateways;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;

public class MsSqlTasksQueueGateway extends DefaultTasksQueueGateway
{
    private static final String CLEAN_TASKS_QUEUE = "DELETE FROM {0} WITH (TABLOCK) WHERE PROCESSING_TIME < DATEADD(ss, {2,number,#}, SYSUTCDATETIME()) AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)";
    private static final String INSERT_TASK_ROW = "INSERT INTO {0} WITH (TABLOCK) (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) AS A WHERE NOT EXISTS (SELECT 1 FROM {0} AS B WHERE A.PK = B.PK)";
    private static final String SELECT_TASKS_WITH_RANGE = "WITH cte AS (SELECT TOP (:limit) t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN {1} ts ON t.PK = ts.PK WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND t.NODE_GROUP = ''{2}'') OR (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= SYSUTCDATETIME() ORDER BY EXECUTION_TIME) UPDATE {0} SET PROCESSING_TIME = DATEADD(ss, :lockDuration, SYSUTCDATETIME()), hjmpts = cte.hjmpTS OUTPUT inserted.PK, inserted.HJMPTS, inserted.PROCESSING_TIME FROM cte WHERE {0}.PK = cte.PK";
    private static final String SELECT_TASKS_EXCLUSIVE_MODE = "WITH cte AS (SELECT TOP (:limit) t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= SYSUTCDATETIME() ORDER BY EXECUTION_TIME) UPDATE {0} SET PROCESSING_TIME = DATEADD(ss, :lockDuration, SYSUTCDATETIME()), hjmpts = cte.hjmpTS OUTPUT inserted.PK, inserted.HJMPTS, inserted.PROCESSING_TIME FROM cte WHERE {0}.PK = cte.PK";
    private static final String DELETE_TASKS_FROM_QUEUE = "DELETE FROM {0} WITH (READPAST) WHERE PK IN (:pks)";
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK BIGINT PRIMARY KEY WITH (IGNORE_DUP_KEY = ON), RANGE_VALUE INT, NODE_ID BIGINT NOT NULL DEFAULT -1, NODE_GROUP NVARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(), EXECUTION_TIME BIGINT, HJMPTS BIGINT)";


    public MsSqlTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate, typeService);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format(
                        "CREATE TABLE {0} (PK BIGINT PRIMARY KEY WITH (IGNORE_DUP_KEY = ON), RANGE_VALUE INT, NODE_ID BIGINT NOT NULL DEFAULT -1, NODE_GROUP NVARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(), EXECUTION_TIME BIGINT, HJMPTS BIGINT)",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected String getCleanTasksQueueStatement(Duration taskProcessingTimeThreshold)
    {
        return MessageFormatUtils.format("DELETE FROM {0} WITH (TABLOCK) WHERE PROCESSING_TIME < DATEADD(ss, {2,number,#}, SYSUTCDATETIME()) AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)", new Object[] {getTableName(), getTableName("Task"),
                        Long.valueOf(taskProcessingTimeThreshold.getSeconds())});
    }


    protected String getInsertTaskRowStatement(String subquery)
    {
        return MessageFormatUtils.format("INSERT INTO {0} WITH (TABLOCK) (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) AS A WHERE NOT EXISTS (SELECT 1 FROM {0} AS B WHERE A.PK = B.PK)", new Object[] {getTableName(), subquery});
    }


    protected String getSelectTasksWithRangeQuery()
    {
        return MessageFormatUtils.format(
                        "WITH cte AS (SELECT TOP (:limit) t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN {1} ts ON t.PK = ts.PK WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND t.NODE_GROUP = ''{2}'') OR (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= SYSUTCDATETIME() ORDER BY EXECUTION_TIME) UPDATE {0} SET PROCESSING_TIME = DATEADD(ss, :lockDuration, SYSUTCDATETIME()), hjmpts = cte.hjmpTS OUTPUT inserted.PK, inserted.HJMPTS, inserted.PROCESSING_TIME FROM cte WHERE {0}.PK = cte.PK",
                        new Object[] {getTableName(), getTableName("Task"),
                                        getEmptyGroupValue()});
    }


    protected String getSelectTasksWithExclusiveModeQuery()
    {
        return MessageFormatUtils.format(
                        "WITH cte AS (SELECT TOP (:limit) t.PK, ts.hjmpTS, t.PROCESSING_TIME FROM {0} t JOIN (SELECT PK, hjmpTS from {1}) ts ON t.PK = ts.PK WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups))) AND PROCESSING_TIME <= SYSUTCDATETIME() ORDER BY EXECUTION_TIME) UPDATE {0} SET PROCESSING_TIME = DATEADD(ss, :lockDuration, SYSUTCDATETIME()), hjmpts = cte.hjmpTS OUTPUT inserted.PK, inserted.HJMPTS, inserted.PROCESSING_TIME FROM cte WHERE {0}.PK = cte.PK",
                        new Object[] {getTableName(), getTableName("Task")});
    }


    protected Optional<String> getLockTasksForProcessingStatementIfNeeded(Duration lockDuration)
    {
        return Optional.empty();
    }


    protected String getDeleteTasksStatement()
    {
        return MessageFormatUtils.format("DELETE FROM {0} WITH (READPAST) WHERE PK IN (:pks)", new Object[] {getTableName()});
    }


    public String getRangeSQLExpression(int rangeStart, int rangeEnd)
    {
        return MessageFormatUtils.format("floor({0,number,#} + (ABS(CAST(CRYPT_GEN_RANDOM(8) AS bigint)) % ({1,number,#}-{0,number,#})))", new Object[] {Integer.valueOf(rangeStart),
                        Integer.valueOf(rangeEnd)});
    }


    protected Map<String, Object> getSelectTasksWithRangeParams(WorkerStateGateway.WorkerRange range, long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Set<String> groups, Duration lockDuration)
    {
        return (Map<String, Object>)ImmutableMap.builder()
                        .putAll(super.getSelectTasksWithRangeParams(range, maxItemsToSchedule, workerState, groups, lockDuration))
                        .put("lockDuration", Long.valueOf(lockDuration.toSeconds()))
                        .build();
    }


    protected Map<String, Object> getSelectTasksWithExclusiveModeParams(long maxItemsToSchedule, WorkerStateGateway.WorkerState workerState, Set<String> groups, Duration lockDuration)
    {
        return (Map<String, Object>)ImmutableMap.builder()
                        .putAll(super.getSelectTasksWithExclusiveModeParams(maxItemsToSchedule, workerState, groups, lockDuration))
                        .put("lockDuration", Long.valueOf(lockDuration.toSeconds()))
                        .build();
    }
}
