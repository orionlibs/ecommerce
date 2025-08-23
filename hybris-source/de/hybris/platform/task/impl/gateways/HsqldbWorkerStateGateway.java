package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class HsqldbWorkerStateGateway extends DefaultWorkerStateGateway
{
    private static final String CREATE_TABLE = "CREATE {1} TABLE {0} (ID BIGINT, RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)";


    public HsqldbWorkerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getCreateTableStatement()
    {
        boolean cachedTable = Config.getBoolean("hsqldb.usecachedtables", false);
        return MessageFormatUtils.format("CREATE {1} TABLE {0} (ID BIGINT, RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)", new Object[] {getTableName(), cachedTable ? "CACHED" : ""});
    }
}
