package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

public class LangReferenceRemoverRunListener extends RunListener
{
    private static final String YDEPLOYMENTS_TABLE = "ydeployments";
    private static final Logger LOG = Logger.getLogger(LangReferenceRemoverRunListener.class.getName());
    private final Set<String> lpTableNames = new LinkedHashSet<>();
    private final Set<String> propertiesTableNames = new LinkedHashSet<>();
    private String languageDeploymenTable;
    private boolean systemInitialized = false;


    public void testStarted(Description description) throws Exception
    {
        JaloConnection con = JaloConnection.getInstance();
        this.systemInitialized = con.isSystemInitialized();
        if(this.systemInitialized)
        {
            JdbcTemplate propsTableDeplQuery = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            propsTableDeplQuery.query("SELECT DISTINCT(propstablename) FROM " + getTablePrefix() + "ydeployments WHERE propstablename is not null", new Object[0], (RowCallbackHandler)new Object(this));
            JdbcTemplate langTableDeplQuery = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            langTableDeplQuery.query("SELECT DISTINCT(tablename) FROM " + getTablePrefix() + "ydeployments WHERE tablename is not null", new Object[0], (RowCallbackHandler)new Object(this));
            this.languageDeploymenTable = ((ComposedType)TypeManager.getInstance().getType("Language")).getTable();
        }
    }


    private String getTablePrefix()
    {
        return Registry.getCurrentTenant().getConfig().getParameter("db.tableprefix");
    }


    public void testFinished(Description description) throws Exception
    {
        if(this.systemInitialized)
        {
            dropOrphanedRows(description, this.lpTableNames);
            dropOrphanedRows(description, this.propertiesTableNames);
        }
    }


    private void dropOrphanedRows(Description description, Set<String> tableNames)
    {
        for(String singleTableName : tableNames)
        {
            try
            {
                JdbcTemplate query = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
                int numOfRemovedLPEntries = query.update("DELETE from " + singleTableName + " WHERE LANGPK IS NOT NULL AND LANGPK <> 0 AND LANGPK NOT IN (SELECT PK FROM " + this.languageDeploymenTable + ")");
                if(numOfRemovedLPEntries > 0)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Removed " + numOfRemovedLPEntries + " of orphaned rows in " + singleTableName + " after test " + description
                                        .getDisplayName());
                    }
                }
            }
            catch(DataAccessException dae)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(dae);
                }
            }
        }
    }
}
