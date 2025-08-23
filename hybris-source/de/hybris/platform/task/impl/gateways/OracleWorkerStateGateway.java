package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleWorkerStateGateway extends DefaultWorkerStateGateway
{
    private static final String INSERT_WORKER_ROW = "INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(SYSDATE, ?, ?, ?)";
    private static final String SELECT_WORKERS_HEALTH_CHECK = "SELECT ID, HEALTH_CHECK, SYSDATE FROM {0}";
    private static final String SELECT_WORKERS = "SELECT ID, HEALTH_CHECK, SYSDATE, EXCLUSIVE_MODE, NODE_GROUPS FROM {0}";
    private static final String UPDATE_WORKER_HEALTH_CHECK = "UPDATE {0} SET HEALTH_CHECK = SYSDATE, EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?";
    private static final String CREATE_WORKERS_STATE_TABLE = "CREATE TABLE {0} (ID VARCHAR(255), RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE NUMBER(1) DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE NUMBER(1))";


    public OracleWorkerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getInsertWorkerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(SYSDATE, ?, ?, ?)", new Object[] {getTableName()});
    }


    protected String getUpdateWorkerHealthCheckStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET HEALTH_CHECK = SYSDATE, EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?", new Object[] {getTableName()});
    }


    protected String getWorkersHealthChecksQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, SYSDATE FROM {0}", new Object[] {getTableName()});
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID VARCHAR(255), RANGES VARCHAR(255), HEALTH_CHECK TIMESTAMP, EXCLUSIVE_MODE NUMBER(1) DEFAULT 0, NODE_GROUPS VARCHAR(255) DEFAULT '''', IS_ACTIVE NUMBER(1))", new Object[] {getTableName()});
    }


    protected String getSelectWorkersQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, SYSDATE, EXCLUSIVE_MODE, NODE_GROUPS FROM {0}", new Object[] {getTableName()});
    }
}
