package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, UTC_TIMESTAMP(3), ?)";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS=UTC_TIMESTAMP WHERE ID = ? AND TIMESTAMPDIFF(MICROSECOND , LAST_ACTIVITY_TS, UTC_TIMESTAMP(3)) / 1000 > ?";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, UTC_TIMESTAMP(3) FROM {0}";


    public MySqlSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, UTC_TIMESTAMP(3), ?)", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, UTC_TIMESTAMP(3) FROM {0}", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS=UTC_TIMESTAMP WHERE ID = ? AND TIMESTAMPDIFF(MICROSECOND , LAST_ACTIVITY_TS, UTC_TIMESTAMP(3)) / 1000 > ?", new Object[] {getTableName()});
    }
}
