package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableSizeCheckRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(TableSizeCheckRunListener.class);
    private static final List<String> excludedTableNames = Arrays.asList(new String[] {"numberseries", "props", "aclentries"});
    private Map<ItemDeployment, Integer> deploymentsMethod;
    private Map<ItemDeployment, Integer> deploymentsClass;
    private Description classDescription;


    private boolean isTableAllowed(String table)
    {
        for(String excludedTableName : excludedTableNames)
        {
            if(table.endsWith(excludedTableName))
            {
                return false;
            }
        }
        return true;
    }


    public void testStarted(Description description) throws Exception
    {
        if(HybrisJUnit4Test.intenseChecksActivated())
        {
            JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            this.deploymentsMethod = new LinkedHashMap<>();
            Set<ItemDeployment> allDeployments = ((DBPersistenceManager)Registry.getPersistenceManager()).getAllDeployments();
            for(ItemDeployment depl : allDeployments)
            {
                String table = depl.getDatabaseTableName();
                if(table != null && isTableAllowed(table))
                {
                    try
                    {
                        Integer oldSize = (Integer)template.queryForObject("SELECT count(*) FROM " + table, Integer.class);
                        this.deploymentsMethod.put(depl, oldSize);
                    }
                    catch(BadSqlGrammarException badSqlGrammarException)
                    {
                    }
                }
            }
        }
    }


    public void testFinished(Description description) throws Exception
    {
        if(HybrisJUnit4Test.intenseChecksActivated())
        {
            JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            for(Map.Entry<ItemDeployment, Integer> entry : this.deploymentsMethod.entrySet())
            {
                String table = ((ItemDeployment)entry.getKey()).getDatabaseTableName();
                try
                {
                    Integer newSize = (Integer)template.queryForObject("SELECT count(*) FROM " + table, Integer.class);
                    if(!newSize.equals(entry.getValue()))
                    {
                        LOG.error("Table size of table " + table + " has changed from " + entry.getValue() + " to " + newSize + " within test case " + description
                                        .getDisplayName() + "\n");
                    }
                }
                catch(BadSqlGrammarException badSqlGrammarException)
                {
                }
            }
        }
    }


    public void testRunStarted(Description description) throws Exception
    {
        if(HybrisJUnit4Test.intenseChecksActivated())
        {
            this.classDescription = description;
            JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            this.deploymentsClass = new LinkedHashMap<>();
            Set<ItemDeployment> allDeployments = ((DBPersistenceManager)Registry.getPersistenceManager()).getAllDeployments();
            for(ItemDeployment depl : allDeployments)
            {
                String table = depl.getDatabaseTableName();
                if(table != null && isTableAllowed(table))
                {
                    try
                    {
                        Integer oldSize = (Integer)template.queryForObject("SELECT count(*) FROM " + table, Integer.class);
                        this.deploymentsClass.put(depl, oldSize);
                    }
                    catch(BadSqlGrammarException badSqlGrammarException)
                    {
                    }
                }
            }
        }
    }


    public void testRunFinished(Result result) throws Exception
    {
        if(HybrisJUnit4Test.intenseChecksActivated())
        {
            JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
            for(Map.Entry<ItemDeployment, Integer> entry : this.deploymentsClass.entrySet())
            {
                String table = ((ItemDeployment)entry.getKey()).getDatabaseTableName();
                try
                {
                    Integer newSize = (Integer)template.queryForObject("SELECT count(*) FROM " + table, Integer.class);
                    if(!newSize.equals(entry.getValue()))
                    {
                        LOG.error("Table size of table " + table + " has changed from " + entry.getValue() + " to " + newSize + " within test class " + this.classDescription
                                        .getClassName() + "\n");
                    }
                }
                catch(BadSqlGrammarException badSqlGrammarException)
                {
                }
            }
        }
    }
}
