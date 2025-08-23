package de.hybris.platform.util.database;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;
import de.hybris.platform.jalo.AbstractSystemCreator;
import de.hybris.platform.util.JspContext;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

public class DropTablesTool
{
    private static final Logger LOGGER = Logger.getLogger(DropTablesTool.class.getName());
    private final Tenant tenant;
    private final JdbcTemplate template;
    private static final AtomicReference<JspContext> localJspContext = new AtomicReference<>();
    private final DatabaseMetaDataCallback metaCallback;


    public DropTablesTool(Tenant tenant, JspContext jspContext)
    {
        this.tenant = tenant;
        this.template = new JdbcTemplate((DataSource)tenant.getDataSource());
        localJspContext.set(jspContext);
        this
                        .metaCallback = (DatabaseMetaDataCallback)new TableNameDatabaseMetaDataCallback((new QueryProviderFactory(tenant.getDataSource().getDatabaseName())).getQueryProviderInstance(), tenant);
    }


    public void dropAllTables() throws SQLException
    {
        try
        {
            dropTables((List<String>)JdbcUtils.extractDatabaseMetaData((DataSource)this.tenant.getDataSource(), this.metaCallback));
        }
        catch(MetaDataAccessException e)
        {
            throw new SQLException(e);
        }
    }


    private void dropTables(List<String> tables)
    {
        boolean skipRecyclebin = skipOracleRecyclebin();
        String query = "DROP TABLE ";
        String tableName = null;
        for(int i = 0, n = tables.size(); i < n; i++)
        {
            try
            {
                tableName = ((String)tables.get(i)).toString();
                if(!tableName.startsWith("BIN$"))
                {
                    this.template.execute("DROP TABLE ".concat(skipRecyclebin ? tableName.concat(" PURGE") : tableName));
                }
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Dropped " + tableName);
                }
            }
            catch(DataAccessException e)
            {
                LOGGER.error("error dropping table '" + tableName + "'." + e, (Throwable)e);
            }
        }
    }


    private boolean skipOracleRecyclebin()
    {
        boolean oracle = false;
        boolean useRecyclebin = false;
        if("oracle".equalsIgnoreCase(this.tenant.getDataSource().getDatabaseName()))
        {
            oracle = true;
            String param = this.tenant.getConfig().getParameter("oracle.use.recyclebin");
            if("true".equalsIgnoreCase(param))
            {
                useRecyclebin = true;
            }
        }
        return (oracle && !useRecyclebin);
    }


    public static void log(String logstring, JspContext ctx)
    {
        AbstractSystemCreator.logln(logstring, ctx);
    }


    public static void log(String logstring)
    {
        log(logstring, localJspContext.get());
    }


    public static void main(String[] args) throws Exception
    {
        (new DropTablesTool(Registry.getCurrentTenant(), null)).dropAllTables();
    }
}
