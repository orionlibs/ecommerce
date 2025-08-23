package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.logger.FileLogger;
import de.hybris.platform.jdbcwrapper.logger.JDBCLogger;
import de.hybris.platform.util.config.ConfigIntf;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

public class JDBCLogUtils
{
    private JDBCLogger logger;
    private SimpleDateFormat dateFormat;
    private int timethreshold;
    private String[] includeTables;
    private String[] excludeTables;
    private String[] includeCategories;
    private String[] excludeCategories;
    private final HybrisDataSource dataSource;
    private final ConfigIntf.ConfigChangeListener cfgChangeListener;
    private boolean loggingActivated;
    private boolean stackappendingActivated;
    private final CopyOnWriteArraySet<StatementsListener> listeners = new CopyOnWriteArraySet<>();


    public JDBCLogUtils(HybrisDataSource dataSource)
    {
        Tenant tenant = dataSource.getTenant();
        this.cfgChangeListener = (ConfigIntf.ConfigChangeListener)new JDBCLogUtilConfigListener(this);
        tenant.getConfig().registerConfigChangeListener(this.cfgChangeListener);
        this.dataSource = dataSource;
        this.loggingActivated = tenant.getConfig().getBoolean("db.log.active", false);
        this.stackappendingActivated = tenant.getConfig().getBoolean("db.log.appendStackTrace", false);
        setLogger(tenant.getConfig().getParameter("db.log.loggerclass"));
        setFilePath(tenant.getConfig().getParameter("db.log.file.path"));
        setIncludeTables(tenant.getConfig().getParameter("db.log.filter.includetables"));
        setExcludeTables(tenant.getConfig().getParameter("db.log.filter.excludetables"));
        setIncludeCategories(tenant.getConfig().getParameter("db.log.includecategories"));
        setExcludeCategories(tenant.getConfig().getParameter("db.log.excludecategories"));
        setDateFormat(tenant.getConfig().getParameter("db.log.dateformat"));
        setTimeThreshold(tenant.getConfig().getParameter("db.log.filter.timethreshold"));
    }


    public boolean isLoggingActivated()
    {
        return (this.loggingActivated || !this.listeners.isEmpty());
    }


    public boolean isStackAppendingActivated()
    {
        return this.stackappendingActivated;
    }


    public void destroy()
    {
        this.dataSource.getTenant().getConfig().unregisterConfigChangeListener(this.cfgChangeListener);
    }


    private void setDateFormat(String value)
    {
        if(value != null)
        {
            this.dateFormat = new SimpleDateFormat(value);
        }
        else
        {
            this.dateFormat = new SimpleDateFormat("yyMMdd HH:mm:ss:SS");
        }
    }


    private void setIncludeCategories(String value)
    {
        if(value == null)
        {
            value = "";
        }
        this.includeCategories = parseCSVList(value);
    }


    private void setExcludeCategories(String value)
    {
        if(value == null)
        {
            value = "";
        }
        this.excludeCategories = parseCSVList(value);
    }


    private void setIncludeTables(String value)
    {
        if(value == null)
        {
            value = "";
        }
        this.includeTables = parseCSVList(value);
    }


    private void setExcludeTables(String value)
    {
        if(value == null)
        {
            value = "";
        }
        this.excludeTables = parseCSVList(value);
    }


    private void setFilePath(String value)
    {
        if(this.logger instanceof FileLogger)
        {
            if(value != null)
            {
                ((FileLogger)this.logger).setLogfile(value);
            }
            else
            {
                ((FileLogger)this.logger).setLogfile("jdbc.log");
            }
        }
    }


    private void setLogger(String value)
    {
        if(value != null)
        {
            try
            {
                Class<?> clazz = Class.forName(value);
                Constructor<?> constructor = clazz.getConstructor(new Class[] {Tenant.class});
                this.logger = (JDBCLogger)constructor.newInstance(new Object[] {this.dataSource.getTenant()});
            }
            catch(Exception e1)
            {
                System.err.println("Warning: Cannot instantiate " + value + ", using default filelogger.");
            }
        }
        else
        {
            this.logger = (JDBCLogger)new FileLogger(this.dataSource.getTenant());
        }
    }


    private void setTimeThreshold(String value)
    {
        if(value == null)
        {
            this.timethreshold = -1;
        }
        else
        {
            this.timethreshold = Integer.parseInt(value);
        }
    }


    private static String[] parseCSVList(String csvList)
    {
        String[] array = null;
        if(csvList != null)
        {
            StringTokenizer tok = new StringTokenizer(csvList, ",");
            ArrayList<String> list = new ArrayList<>();
            while(tok.hasMoreTokens())
            {
                String item = tok.nextToken().toLowerCase().trim();
                if(item.length() > 0)
                {
                    list.add(item.toLowerCase().trim());
                }
            }
            int max = list.size();
            Iterator<String> iterator = list.iterator();
            array = new String[max];
            for(int index = 0; index < max; index++)
            {
                array[index] = iterator.next();
            }
        }
        return array;
    }


    private boolean isCategoryOk(String category)
    {
        return ((this.includeCategories == null || this.includeCategories.length == 0 || foundCategory(category, this.includeCategories)) &&
                        !foundCategory(category, this.excludeCategories));
    }


    private boolean foundCategory(String category, String[] categories)
    {
        if(categories != null)
        {
            for(String element : categories)
            {
                if(category.equals(element))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean queryOk(String sql)
    {
        return ((this.includeTables == null || this.includeTables.length == 0 || foundTable(sql, this.includeTables)) &&
                        !foundTable(sql, this.excludeTables));
    }


    private boolean foundTable(String sql, String[] tables)
    {
        sql = sql.toLowerCase();
        boolean isOk = false;
        if(tables != null)
        {
            for(int i = 0; !isOk && i < tables.length; i++)
            {
                isOk = (sql.indexOf(tables[i]) > 0);
            }
        }
        return isOk;
    }


    private boolean meetsThresholdRequirement(long timeTaken)
    {
        if(this.timethreshold <= 0)
        {
            return true;
        }
        return (timeTaken > this.timethreshold);
    }


    private void doLog(long threadId, long elapsed, String category, String prepared, String sql)
    {
        doLog(threadId, -1, elapsed, category, prepared, sql);
    }


    private void doLogElapsed(long threadId, int connectionId, long startTime, long endTime, String category, String prepared, String sql)
    {
        doLog(threadId, connectionId, endTime - startTime, category, prepared, sql);
    }


    private synchronized void doLog(long threadId, int connectionId, long elapsed, String category, String prepared, String sql)
    {
        if(this.logger != null)
        {
            String stringNow;
            if(this.dateFormat == null)
            {
                stringNow = Long.toString(System.currentTimeMillis());
            }
            else
            {
                stringNow = this.dateFormat.format(new Date(System.currentTimeMillis())).trim();
            }
            this.logger.logSQL(threadId, this.dataSource.getID(), connectionId, stringNow, elapsed, category, prepared, sql);
        }
    }


    public void log(long threadId, String category, String prepared, String sql)
    {
        if(this.logger != null && isCategoryOk(category))
        {
            doLog(threadId, -1L, category, prepared, sql);
        }
    }


    public void logElapsed(long threadId, int connectionId, long startTime, String category, String prepared, String sql)
    {
        long endTime = System.currentTimeMillis();
        this.listeners.forEach(s -> s.statementExecuted(sql));
        if(this.logger != null && queryOk(sql) && meetsThresholdRequirement(endTime - startTime) && isCategoryOk(category))
        {
            doLogElapsed(threadId, connectionId, startTime, endTime, category, prepared, sql);
        }
    }


    void addListener(StatementsListener listener)
    {
        this.listeners.add(Objects.<StatementsListener>requireNonNull(listener));
    }


    void removeListener(StatementsListener listener)
    {
        this.listeners.remove(Objects.requireNonNull(listener));
    }
}
