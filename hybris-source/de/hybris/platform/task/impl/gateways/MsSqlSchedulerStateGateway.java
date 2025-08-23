package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class MsSqlSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (ID NVARCHAR(255), LAST_ACTIVITY_TS datetime2(3), VERSION INT, unique (ID));";
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, GETUTCDATE(), ?)";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, GETUTCDATE() FROM {0}";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS=GETUTCDATE() WHERE ID = ? AND DATEDIFF_BIG(ms, LAST_ACTIVITY_TS, GETUTCDATE()) > ?";


    public MsSqlSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID NVARCHAR(255), LAST_ACTIVITY_TS datetime2(3), VERSION INT, unique (ID));", new Object[] {getTableName()});
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, GETUTCDATE(), ?)", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, GETUTCDATE() FROM {0}", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS=GETUTCDATE() WHERE ID = ? AND DATEDIFF_BIG(ms, LAST_ACTIVITY_TS, GETUTCDATE()) > ?", new Object[] {getTableName()});
    }
}
