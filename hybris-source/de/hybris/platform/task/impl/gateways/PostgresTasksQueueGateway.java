package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.MessageFormatUtils;
import java.time.Duration;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;

public class PostgresTasksQueueGateway extends DefaultTasksQueueGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT NOT NULL DEFAULT -1, NODE_GROUP VARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME TIMESTAMP(0) NOT NULL DEFAULT DATE_TRUNC(''SECOND'', NOW()), EXECUTION_TIME BIGINT)";
    private static final String CLEAN_TASKS_QUEUE = "DELETE FROM {0} WHERE PROCESSING_TIME < NOW() - INTERVAL ''{2,number,#} SECOND'' AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)";
    private static final String LOCK_TASKS_FOR_PROCESSING = "UPDATE {0} SET PROCESSING_TIME = now() + interval ''{1,number,#} SECOND'' WHERE PK = ? AND PROCESSING_TIME = ?";


    public PostgresTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate, typeService);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT NOT NULL DEFAULT -1, NODE_GROUP VARCHAR(255) NOT NULL DEFAULT ''{1}'', PROCESSING_TIME TIMESTAMP(0) NOT NULL DEFAULT DATE_TRUNC(''SECOND'', NOW()), EXECUTION_TIME BIGINT)",
                        new Object[] {getTableName(), getEmptyGroupValue()});
    }


    protected String getCleanTasksQueueStatement(Duration taskProcessingTimeThreshold)
    {
        return MessageFormatUtils.format("DELETE FROM {0} WHERE PROCESSING_TIME < NOW() - INTERVAL ''{2,number,#} SECOND'' AND NOT EXISTS (SELECT 1 FROM {1} t2 WHERE {0}.PK = t2.PK)", new Object[] {getTableName(), getTableName("Task"),
                        Long.valueOf(taskProcessingTimeThreshold.getSeconds())});
    }


    public String getRangeSQLExpression(int rangeStart, int rangeEnd)
    {
        return MessageFormatUtils.format("floor({0,number,#}+ (random()*({1,number,#}-{0,number,#})))", new Object[] {Integer.valueOf(rangeStart), Integer.valueOf(rangeEnd)});
    }


    protected Optional<String> getLockTasksForProcessingStatementIfNeeded(Duration lockDuration)
    {
        return Optional.of(MessageFormatUtils.format("UPDATE {0} SET PROCESSING_TIME = now() + interval ''{1,number,#} SECOND'' WHERE PK = ? AND PROCESSING_TIME = ?", new Object[] {getTableName(), Long.valueOf(lockDuration.getSeconds())}));
    }
}
