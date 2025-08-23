package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.MessageFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DefaultBaseGateway implements BaseGateway
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBaseGateway.class);
    protected final JdbcTemplate jdbcTemplate;


    public DefaultBaseGateway(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    static String getTenantAwareTableName(String tableName)
    {
        if("master".equals(Registry.getCurrentTenant().getTenantID()))
        {
            return tableName;
        }
        return Registry.getCurrentTenant().getTenantID() + "_" + Registry.getCurrentTenant().getTenantID();
    }


    public boolean createTable()
    {
        try
        {
            this.jdbcTemplate.execute(getCreateTableStatement());
            return true;
        }
        catch(DataAccessException ex)
        {
            LOGGER.warn("error while creating table {}", getTableName(), ex);
            return false;
        }
    }


    public void dropTable()
    {
        this.jdbcTemplate.execute(MessageFormatUtils.format("DROP TABLE {0}", new Object[] {getTableName()}));
    }


    public boolean doesTableExist()
    {
        try
        {
            Integer t = (Integer)this.jdbcTemplate.queryForObject(MessageFormatUtils.format("SELECT 1, count(*) FROM {0}", new Object[] {getTableName()}), (resultSet, i) -> Integer.valueOf(resultSet.getInt(1)));
            return (t.intValue() == 1);
        }
        catch(BadSqlGrammarException ex)
        {
            LOGGER.info("problem while checking whether {} table exists: {}", getTableName(), ex
                            .getSQLException().getMessage());
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("stacktrace", (Throwable)ex);
            }
            return false;
        }
    }


    protected boolean tryCreatingTableAndLogException(Logger logger, String message, Exception ex)
    {
        if(createTable())
        {
            logger.info("{} - creating table was successful", message);
            return true;
        }
        if(logger.isDebugEnabled())
        {
            logger.warn("{}", message, ex);
        }
        else
        {
            logger.warn("{}: {}", message, ex.getMessage());
        }
        return false;
    }


    protected JdbcTemplate getJdbcTemplate()
    {
        return this.jdbcTemplate;
    }


    protected abstract String getCreateTableStatement();
}
