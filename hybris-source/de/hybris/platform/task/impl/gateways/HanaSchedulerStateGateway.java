package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class HanaSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS TIMESTAMP, VERSION BIGINT, UNIQUE (ID))";
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, CURRENT_UTCTIMESTAMP, ?)";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, CURRENT_UTCTIMESTAMP FROM {0}";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS = CURRENT_UTCTIMESTAMP WHERE ID = ? AND SECONDS_BETWEEN(LAST_ACTIVITY_TS, CURRENT_UTCTIMESTAMP) * 1000 >= ?";


    public HanaSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID VARCHAR(255), LAST_ACTIVITY_TS TIMESTAMP, VERSION BIGINT, UNIQUE (ID))", new Object[] {getTableName()});
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, CURRENT_UTCTIMESTAMP, ?)", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, CURRENT_UTCTIMESTAMP FROM {0}", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS = CURRENT_UTCTIMESTAMP WHERE ID = ? AND SECONDS_BETWEEN(LAST_ACTIVITY_TS, CURRENT_UTCTIMESTAMP) * 1000 >= ?", new Object[] {getTableName()});
    }
}
