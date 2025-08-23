package de.hybris.platform.jdbcwrapper;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

public final class OracleStatementCachingConfigurer
{
    private static final Logger LOG = Logger.getLogger(OracleStatementCachingConfigurer.class.getName());
    private static final String ORACLE_ENABLE_CACHING_METHOD = "setImplicitCachingEnabled";
    private static final Class[] ORACLE_ENABLE_CACHING_METHOD_SIG = new Class[] {boolean.class};
    private static final Object[] ORACLE_ENABLE_CACHING_METHOD_ARGS = new Object[] {Boolean.TRUE};
    private static final String ORACLE_CACHING_SIZE_METHOD = "setStatementCacheSize";
    private static final Class[] ORACLE_CACHING_SIZE_METHOD_SIG = new Class[] {int.class};
    private static final String ORACLE_CHECK_ENABLE_CACHING_METHOD = "getImplicitCachingEnabled";
    private static final Class[] ORACLE_CHECK_ENABLE_CACHING_METHOD_SIG = null;
    private static final Object[] ORACLE_CHECK_ENABLE_CACHING_METHOD_ARGS = null;
    private static final Object[] ORACLE_DISABLE_CACHING_METHOD_ARGS = new Object[] {Boolean.FALSE};
    private static final String ORACLE_BASE_CONNECTION_CLASS_NAME = "oracle.jdbc.OracleConnectionWrapper";
    private final transient Map<Class, Method> enablementMethodCache = (Map)new ConcurrentHashMap<>();
    private final transient Map<Class, Method> sizeMethodCache = (Map)new ConcurrentHashMap<>();
    private final transient Map<Class, Method> checkEnabledMethodCache = (Map)new ConcurrentHashMap<>();
    private volatile Boolean oracleDatabaseMode;
    private volatile Boolean oracleStatementCachingMode = null;
    private int oracleStatementCacheSize = -1;
    private final HybrisDataSource dataSource;


    public OracleStatementCachingConfigurer(HybrisDataSource dataSource)
    {
        this.dataSource = dataSource;
    }


    private boolean isOracleMode()
    {
        if(this.oracleDatabaseMode == null)
        {
            this.oracleDatabaseMode = Boolean.valueOf("oracle".equalsIgnoreCase(this.dataSource.getDatabaseName()));
        }
        return this.oracleDatabaseMode.booleanValue();
    }


    public synchronized void enableOracleStatementCaching(int cacheSize)
    {
        if(isOracleMode())
        {
            if(cacheSize > 0)
            {
                this.oracleStatementCacheSize = cacheSize;
                this.oracleStatementCachingMode = Boolean.TRUE;
            }
            else
            {
                disableOracleStatementCaching();
            }
        }
    }


    public synchronized void disableOracleStatementCaching()
    {
        if(isOracleMode())
        {
            this.oracleStatementCacheSize = -1;
            this.oracleStatementCachingMode = Boolean.FALSE;
        }
    }


    public synchronized void resetOracleStatementCaching()
    {
        if(isOracleMode())
        {
            this.oracleStatementCacheSize = -1;
            this.oracleStatementCachingMode = null;
        }
    }


    public boolean isOracleStatementCachingEnabled()
    {
        if(isOracleMode())
        {
            Boolean ret = this.oracleStatementCachingMode;
            if(ret == null)
            {
                synchronized(this)
                {
                    ret = this.oracleStatementCachingMode;
                    if(ret == null)
                    {
                        Boolean mode = Boolean.FALSE;
                        int tmp = this.dataSource.getTenant().getConfig().getInt("oracle.statementcachesize", 0);
                        if(tmp > 0)
                        {
                            this.oracleStatementCacheSize = tmp;
                            mode = Boolean.TRUE;
                        }
                        ret = mode;
                        this.oracleStatementCachingMode = ret;
                    }
                }
            }
            return ret.booleanValue();
        }
        return false;
    }


    public void applyOracleStatementCachingSettings(Connection conn)
    {
        if(isOracleMode())
        {
            if(isOracleStatementCachingEnabled())
            {
                Connection sqlCon = conn;
                if(!isCachingEnabled(sqlCon))
                {
                    if(isCachingSupported(sqlCon.getClass()))
                    {
                        enableOracleStatementCaching(sqlCon, this.oracleStatementCacheSize);
                    }
                }
            }
            else
            {
                Connection sqlCon = conn;
                if(isCachingEnabled(sqlCon))
                {
                    disableOracleStatementCaching(conn);
                }
            }
        }
    }


    private Method getEnablementMethod(Class clazz)
    {
        Method ret = this.enablementMethodCache.get(clazz);
        if(ret == null)
        {
            try
            {
                ret = clazz.getMethod("setImplicitCachingEnabled", ORACLE_ENABLE_CACHING_METHOD_SIG);
                ret.setAccessible(true);
                this.enablementMethodCache.put(clazz, ret);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }


    private Method getSizeMethod(Class clazz)
    {
        Method ret = this.sizeMethodCache.get(clazz);
        if(ret == null)
        {
            try
            {
                ret = clazz.getMethod("setStatementCacheSize", ORACLE_CACHING_SIZE_METHOD_SIG);
                ret.setAccessible(true);
                this.sizeMethodCache.put(clazz, ret);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }


    private Method getCheckMethod(Class clazz)
    {
        Method ret = this.checkEnabledMethodCache.get(clazz);
        if(ret == null)
        {
            try
            {
                ret = clazz.getMethod("getImplicitCachingEnabled", ORACLE_CHECK_ENABLE_CACHING_METHOD_SIG);
                ret.setAccessible(true);
                this.checkEnabledMethodCache.put(clazz, ret);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }


    private boolean isCachingEnabled(Connection sqlConnection)
    {
        try
        {
            Boolean enabled = Boolean.FALSE;
            Class<?> className = sqlConnection.getClass();
            if(isCachingSupported(className))
            {
                enabled = (Boolean)getCheckMethod(className).invoke(sqlConnection, ORACLE_CHECK_ENABLE_CACHING_METHOD_ARGS);
            }
            return Boolean.TRUE.equals(enabled);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private boolean isCachingSupported(Class<?> connectionClass)
    {
        try
        {
            return Class.forName("oracle.jdbc.OracleConnectionWrapper").isAssignableFrom(connectionClass);
        }
        catch(ClassNotFoundException clnfe)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not find the class being checked as caching supported ," + clnfe.getMessage(), clnfe);
            }
            return false;
        }
    }


    private void enableOracleStatementCaching(Connection sqlConnection, int cacheSize)
    {
        try
        {
            Class<?> clazz = sqlConnection.getClass();
            getEnablementMethod(clazz).invoke(sqlConnection, ORACLE_ENABLE_CACHING_METHOD_ARGS);
            getSizeMethod(clazz).invoke(sqlConnection, new Object[] {Integer.valueOf(cacheSize)});
            LOG.warn("Oracle Statement cache activated. If you encounter protocol violations or 'inconsistent state' warnings from the jdbc driver, turn this feature off by setting oracle.statementcachesize=0 ");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private void disableOracleStatementCaching(Connection sqlConnection)
    {
        try
        {
            Class<?> clazz = sqlConnection.getClass();
            getEnablementMethod(clazz).invoke(sqlConnection, ORACLE_DISABLE_CACHING_METHOD_ARGS);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
