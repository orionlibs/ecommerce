package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class PostgresSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, now() at time zone ''utc'', ?)";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, now() at time zone ''utc'' FROM {0}";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS = now() at time zone ''utc'' WHERE ID = ? AND (EXTRACT(EPOCH from now() at time zone ''utc'') - EXTRACT(EPOCH FROM LAST_ACTIVITY_TS)) * 1000 > ?";


    public PostgresSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, now() at time zone ''utc'', ?)", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, now() at time zone ''utc'' FROM {0}", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS = now() at time zone ''utc'' WHERE ID = ? AND (EXTRACT(EPOCH from now() at time zone ''utc'') - EXTRACT(EPOCH FROM LAST_ACTIVITY_TS)) * 1000 > ?", new Object[] {getTableName()});
    }
}
