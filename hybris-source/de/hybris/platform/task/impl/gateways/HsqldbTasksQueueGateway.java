package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class HsqldbTasksQueueGateway extends DefaultTasksQueueGateway
{
    private static final String CREATE_TABLE = "CREATE {2} TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT NOW() NOT NULL, EXECUTION_TIME BIGINT)";


    public HsqldbTasksQueueGateway(JdbcTemplate jdbcTemplate, TypeService typeService)
    {
        super(jdbcTemplate, typeService);
    }


    protected String getCreateTableStatement()
    {
        boolean cachedTable = Config.getBoolean("hsqldb.usecachedtables", false);
        return MessageFormatUtils.format("CREATE {2} TABLE {0} (PK BIGINT PRIMARY KEY, RANGE_VALUE INT, NODE_ID BIGINT DEFAULT -1 NOT NULL, NODE_GROUP VARCHAR(255) DEFAULT ''{1}'' NOT NULL, PROCESSING_TIME TIMESTAMP(0) DEFAULT NOW() NOT NULL, EXECUTION_TIME BIGINT)",
                        new Object[] {getTableName(), getEmptyGroupValue(), cachedTable ? "CACHED" : ""});
    }
}
