package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleTasksQueueGateway extends DefaultTasksQueueGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK NUMBER(20) PRIMARY KEY, RANGE_VALUE NUMBER(5), NODE_ID NUMBER(10) DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT SYSDATE NOT NULL, EXECUTION_TIME NUMBER(10))";
    private static final String SELECT_TASKS_WITH_RANGE = "SELECT t1.PK, ts1.HJMPTS, T1.PROCESSING_TIME\nFROM {0} t1,{1} ts1\nWHERE (t1.ROWID, ts1.ROWID) in (SELECT t.ROWID a, ts.ROWID b\n                                FROM {0} t, {1} ts\n                                WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND\n                                        t.NODE_GROUP = ''{2}'') OR\n                                       (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups)))\n                                  AND t.PROCESSING_TIME <= SYSDATE\n                                  AND t.PK = ts.PK\n                                ORDER BY t.EXECUTION_TIME\n                                    FETCH FIRST :limit ROWS ONLY\n)\n    FOR UPDATE OF PROCESSING_TIME";
    private static final String SELECT_TASKS_EXCLUSIVE_MODE = "SELECT t1.PK, ts1.HJMPTS, T1.PROCESSING_TIME\nFROM {0} t1,\n     {1} ts1\nWHERE (t1.ROWID, ts1.ROWID) in (SELECT t.ROWID a, ts.ROWID b\n                                FROM {0} t\n                                         JOIN {1} ts ON t.PK = ts.PK\n                                WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups)))\n                                  AND PROCESSING_TIME <= SYSDATE\n                                ORDER BY EXECUTION_TIME\n                                    FETCH FIRST :limit ROWS ONLY\n)\n    FOR UPDATE OF PROCESSING_TIME";
    private static final String INSERT_TASK_ROW = "INSERT INTO {0} (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) A WHERE NOT EXISTS (SELECT 1 FROM {0}  B WHERE A.PK = B.PK)";
    private static final String CLEAN_TASKS_QUEUE = "DELETE FROM {0} WHERE PROCESSING_TIME < SYSDATE - INTERVAL ''{2,number,#}'' SECOND AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)";
    private static final String LOCK_TASKS_FOR_PROCESSING = "UPDATE {0} SET PROCESSING_TIME = SYSDATE + INTERVAL ''{1,number,#}'' SECOND WHERE PK = ? AND PROCESSING_TIME = ?";


    public OracleTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate, typeService);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (PK NUMBER(20) PRIMARY KEY, RANGE_VALUE NUMBER(5), NODE_ID NUMBER(10) DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT SYSDATE NOT NULL, EXECUTION_TIME NUMBER(10))",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected String getSelectTasksWithRangeQuery()
    {
        return MessageFormatUtils.format(
                        "SELECT t1.PK, ts1.HJMPTS, T1.PROCESSING_TIME\nFROM {0} t1,{1} ts1\nWHERE (t1.ROWID, ts1.ROWID) in (SELECT t.ROWID a, ts.ROWID b\n                                FROM {0} t, {1} ts\n                                WHERE ((t.RANGE_VALUE BETWEEN :rangeStart AND :rangeEnd AND t.NODE_ID = -1 AND\n                                        t.NODE_GROUP = ''{2}'') OR\n                                       (t.NODE_ID = :nodeId OR t.NODE_GROUP IN (:nodeGroups)))\n                                  AND t.PROCESSING_TIME <= SYSDATE\n                                  AND t.PK = ts.PK\n                                ORDER BY t.EXECUTION_TIME\n                                    FETCH FIRST :limit ROWS ONLY\n)\n    FOR UPDATE OF PROCESSING_TIME",
                        new Object[] {getTableName(), getTableName("Task"),
                                        getEmptyGroupValue()});
    }


    protected String getCleanTasksQueueStatement(Duration taskProcessingTimeThreshold)
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE PROCESSING_TIME < SYSDATE - INTERVAL ''{2,number,#}'' SECOND AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)", new Object[] {getTableName(), getTableName("Task"),
                        Long.valueOf(taskProcessingTimeThreshold.getSeconds())});
    }


    public String getRangeSQLExpression(int rangeStart, int rangeEnd)
    {
        return MessageFormatUtils.format("floor({0,number,#}+ (dbms_random.value()*({1,number,#}-{0,number,#})))", new Object[] {Integer.valueOf(rangeStart),
                        Integer.valueOf(rangeEnd)});
    }


    protected String getInsertTaskRowStatement(String subquery)
    {
        return MessageFormatUtils.format("INSERT INTO {0} (PK, RANGE_VALUE, NODE_ID, NODE_GROUP, EXECUTION_TIME) SELECT * FROM ({1}) A WHERE NOT EXISTS (SELECT 1 FROM {0}  B WHERE A.PK = B.PK)", new Object[] {getTableName(), subquery});
    }


    protected String getSelectTasksWithExclusiveModeQuery()
    {
        return MessageFormatUtils.format(
                        "SELECT t1.PK, ts1.HJMPTS, T1.PROCESSING_TIME\nFROM {0} t1,\n     {1} ts1\nWHERE (t1.ROWID, ts1.ROWID) in (SELECT t.ROWID a, ts.ROWID b\n                                FROM {0} t\n                                         JOIN {1} ts ON t.PK = ts.PK\n                                WHERE (t.NODE_ID = :nodeId OR (t.NODE_ID = -1 AND t.NODE_GROUP IN (:nodeGroups)))\n                                  AND PROCESSING_TIME <= SYSDATE\n                                ORDER BY EXECUTION_TIME\n                                    FETCH FIRST :limit ROWS ONLY\n)\n    FOR UPDATE OF PROCESSING_TIME",
                        new Object[] {getTableName(), getTableName("Task")});
    }


    protected Optional<String> getLockTasksForProcessingStatementIfNeeded(Duration lockDuration)
    {
        return Optional.of(MessageFormatUtils.format("UPDATE {0} SET PROCESSING_TIME = SYSDATE + INTERVAL ''{1,number,#}'' SECOND WHERE PK = ? AND PROCESSING_TIME = ?", new Object[] {getTableName(), Long.valueOf(lockDuration.getSeconds())}));
    }
}
