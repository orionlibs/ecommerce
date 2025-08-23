package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.util.MessageFormatUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class MsSqlWorkerStateGateway extends DefaultWorkerStateGateway
{
    private static final String CREATE_TABLE = "CREATE TABLE {0} (ID NVARCHAR(255), RANGES NVARCHAR(255), HEALTH_CHECK DATETIME2, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS NVARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)";
    private static final String UPDATE_WORKER_HEALTH_CHECK = "UPDATE {0} SET HEALTH_CHECK = SYSUTCDATETIME(), EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?";
    private static final String SELECT_WORKERS_HEALTH_CHECK = "SELECT ID, HEALTH_CHECK, SYSUTCDATETIME() FROM {0}";
    private static final String SELECT_WORKERS = "SELECT ID, HEALTH_CHECK, SYSUTCDATETIME(), EXCLUSIVE_MODE, NODE_GROUPS FROM {0}";
    private static final String INSERT_WORKER_ROW = "INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(SYSUTCDATETIME(), ?, ?, ?)";


    public MsSqlWorkerStateGateway(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }


    protected String getCreateTableStatement()
    {
        return MessageFormatUtils.format("CREATE TABLE {0} (ID NVARCHAR(255), RANGES NVARCHAR(255), HEALTH_CHECK DATETIME2, EXCLUSIVE_MODE TINYINT DEFAULT 0, NODE_GROUPS NVARCHAR(255) DEFAULT '''', IS_ACTIVE TINYINT DEFAULT 0)", new Object[] {getTableName()});
    }


    protected String getUpdateWorkerHealthCheckStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET HEALTH_CHECK = SYSUTCDATETIME(), EXCLUSIVE_MODE = ?, NODE_GROUPS = ? WHERE ID = ?", new Object[] {getTableName()});
    }


    protected String getWorkersHealthChecksQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, SYSUTCDATETIME() FROM {0}", new Object[] {getTableName()});
    }


    protected String getSelectWorkersQuery()
    {
        return MessageFormatUtils.format("SELECT ID, HEALTH_CHECK, SYSUTCDATETIME(), EXCLUSIVE_MODE, NODE_GROUPS FROM {0}", new Object[] {getTableName()});
    }


    protected String getInsertWorkerRowStatement()
    {
        return MessageFormatUtils.format("INSERT INTO {0} (HEALTH_CHECK, EXCLUSIVE_MODE, NODE_GROUPS, ID) VALUES(SYSUTCDATETIME(), ?, ?, ?)", new Object[] {getTableName()});
    }
}
