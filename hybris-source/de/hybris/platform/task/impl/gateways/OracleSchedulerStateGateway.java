package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleSchedulerStateGateway extends DefaultSchedulerStateGateway
{
    private static final String INSERT_SCHEDULER_ROW = "INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, SYS_EXTRACT_UTC(SYSTIMESTAMP), ?)";
    private static final String SELECT_SCHEDULER_TIMESTAMP = "SELECT LAST_ACTIVITY_TS, VERSION, CAST(SYS_EXTRACT_UTC(SYSTIMESTAMP) AS TIMESTAMP) FROM {0}";
    private static final String UPDATE_SCHEDULER_ROW_BY_DURATION = "UPDATE {0} SET LAST_ACTIVITY_TS=SYS_EXTRACT_UTC(SYSTIMESTAMP) WHERE ID = ? AND LAST_ACTIVITY_TS < (SYS_EXTRACT_UTC(SYSTIMESTAMP) - NUMTODSINTERVAL(?/1000, ''SECOND''))";


    public OracleSchedulerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getInsertSchedulerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (ID, LAST_ACTIVITY_TS, VERSION) VALUES(?, SYS_EXTRACT_UTC(SYSTIMESTAMP), ?)", new Object[] {getTableName()});
    }


    protected String getSelectSchedulerTimestampQuery()
    {
        return MessageFormatUtils.format("SELECT LAST_ACTIVITY_TS, VERSION, CAST(SYS_EXTRACT_UTC(SYSTIMESTAMP) AS TIMESTAMP) FROM {0}", new Object[] {getTableName()});
    }


    protected String getUpdateSchedulerRowByDurationStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET LAST_ACTIVITY_TS=SYS_EXTRACT_UTC(SYSTIMESTAMP) WHERE ID = ? AND LAST_ACTIVITY_TS < (SYS_EXTRACT_UTC(SYSTIMESTAMP) - NUMTODSINTERVAL(?/1000, ''SECOND''))", new Object[] {getTableName()});
    }
}
