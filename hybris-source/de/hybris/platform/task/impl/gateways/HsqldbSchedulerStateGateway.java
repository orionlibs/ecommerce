package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class HsqldbSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String CREATE_TABLE = "CREATE {1} TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS timestamp(3), VERSION INT, unique (ID))";
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, now(), ?)";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS = now() WHERE ID = ? AND DATEDIFF(MS, LAST_ACTIVITY_TS, now()) > ?";


    public HsqldbSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getCreateTableStatement()
    {
        boolean cachedTable = Config.getBoolean("hsqldb.usecachedtables", false);
        return MessageFormatUtils.format("CREATE {1} TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS timestamp(3), VERSION INT, unique (ID))", new Object[] {getTableName(), cachedTable ? "CACHED" : ""});
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, now(), ?)", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS = now() WHERE ID = ? AND DATEDIFF(MS, LAST_ACTIVITY_TS, now()) > ?", new Object[] {getTableName()});
    }
}
