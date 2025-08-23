package de.hybris.platform.jdbcwrapper;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import de.hybris.platform.jdbcwrapper.interceptor.factory.JDBCInterceptorFactory;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class DataSourceImpl implements HybrisDataSource
{
    private static final Logger LOG = Logger.getLogger(DataSourceImpl.class.getName());
    private final Tenant tenant;
    private final boolean readOnly;
    private final JDBCConnectionPool connectionPool;
    private final String id;
    private final JDBCLogUtils logUtils;
    private final String jndiName;
    private final DataSourceFactory factory;
    private final Map<String, String> connectionParameters;
    private final int maxPreparedParameterCount;
    private volatile DataSourceInfo info = new DataSourceInfo();
    private final AtomicLong getCounter = new AtomicLong(0L);
    private final CloseOnRollbackAfterError closeConnectionOnRollbackAfterError;
    private SQLErrorCodeSQLExceptionTranslator exceptionTranslator;
    protected static final Integer OBJ_POOL_V1_WHEN_EXHAUSTED_FAIL = Integer.valueOf(0);
    protected static final Integer OBJ_POOL_V1_WHEN_EXHAUSTED_BLOCK = Integer.valueOf(1);
    protected static final Integer OBJ_POOL_V1_WHEN_EXHAUSTED_GROW = Integer.valueOf(2);
    private final JDBCInterceptor jdbcInterceptor;


    private boolean isCloseConnetionOnRollbackAfterError()
    {
        switch(null.$SwitchMap$de$hybris$platform$jdbcwrapper$DataSourceImpl$CloseOnRollbackAfterError[this.closeConnectionOnRollbackAfterError.ordinal()])
        {
            case 1:
                return false;
            case 2:
                return true;
            case 3:
                return "mysql".equalsIgnoreCase(getDatabaseName());
        }
        return false;
    }


    public String getDriverVersion()
    {
        return this.info.driverVersion;
    }


    private long millisWaitedForConnection = 0L;


    public DataSourceImpl(Tenant tenant, String id, Map<String, String> params, boolean readOnly, DataSourceFactory factory)
    {
        this(tenant, id, params, null, readOnly, factory);
    }


    public DataSourceImpl(Tenant tenant, String id, String jndiName, boolean readOnly, DataSourceFactory factory)
    {
        this(tenant, id, null, jndiName, readOnly, factory);
    }


    protected DataSourceImpl(Tenant tenant, String id, Map<String, String> params, String jndiName, boolean readOnly, DataSourceFactory factory)
    {
        if(tenant == null)
        {
            throw new IllegalArgumentException("tenant was null");
        }
        this.tenant = tenant;
        this.factory = factory;
        this.readOnly = readOnly;
        this.jndiName = jndiName;
        this.id = id;
        this.jdbcInterceptor = JDBCInterceptorFactory.create(tenant);
        this.closeConnectionOnRollbackAfterError = CloseOnRollbackAfterError.getCloseOnRollbackMode(tenant.getConfig().getString("db.pool.closeOnRollbackAfterError", null));
        if(jndiName == null)
        {
            checkParams(params);
            updateDbURLParam(params);
            this.connectionParameters = Collections.unmodifiableMap(new HashMap<>(params));
            String databaseTablePrefix = params.get(Config.SystemSpecificParams.DB_TABLEPREFIX);
            String databaseURL = params.get(Config.SystemSpecificParams.DB_URL);
            String databaseUser = params.get(Config.SystemSpecificParams.DB_USERNAME);
            String databaseName = DatabaseNameResolver.guessDatabaseNameFromURL(databaseURL);
            String customsessionsql = params.get("db.customsessionsql");
            this.info = new DataSourceInfo(databaseName, customsessionsql, databaseURL, databaseUser, databaseTablePrefix, this.info.databaseSchemaName, this.info.databaseVersion, this.info.driverVersion);
        }
        else
        {
            this.connectionParameters = null;
        }
        this.logUtils = new JDBCLogUtils(this);
        JDBCConnectionPool pool = this.factory.createConnectionPool(this, createNewPoolConfig());
        boolean successfullyConnected = false;
        try
        {
            this.info = adjustDatabaseInfos(tenant, pool, this.info, jndiName, this.connectionParameters, readOnly);
            this.connectionPool = pool;
            successfullyConnected = true;
        }
        finally
        {
            if(!successfullyConnected)
            {
                pool.close();
            }
        }
        this.maxPreparedParameterCount = calculateMaxPreparedStatementCount();
    }


    private void updateDbURLParam(Map<String, String> params)
    {
        params.put(Config.SystemSpecificParams.DB_URL, Utilities.applySpecialPropertiesToDatabaseUrl(params.get(Config.SystemSpecificParams.DB_URL)));
    }


    private void checkParams(Map<String, String> params)
    {
        if(!params.containsKey(Config.SystemSpecificParams.DB_DRIVER))
        {
            throw new IllegalArgumentException("missing connection parameter " + Config.SystemSpecificParams.DB_DRIVER + " in " + params);
        }
        if(!params.containsKey(Config.SystemSpecificParams.DB_USERNAME))
        {
            throw new IllegalArgumentException("missing connection parameter " + Config.SystemSpecificParams.DB_USERNAME + " in " + params);
        }
        if(!params.containsKey(Config.SystemSpecificParams.DB_PASSWORD))
        {
            throw new IllegalArgumentException("missing connection parameter " + Config.SystemSpecificParams.DB_PASSWORD + " in " + params);
        }
        if(!params.containsKey(Config.SystemSpecificParams.DB_TABLEPREFIX))
        {
            throw new IllegalArgumentException("missing connection parameter " + Config.SystemSpecificParams.DB_TABLEPREFIX + " in " + params);
        }
        if(!params.containsKey(Config.SystemSpecificParams.DB_URL))
        {
            throw new IllegalArgumentException("missing connection parameter " + Config.SystemSpecificParams.DB_URL + " in " + params);
        }
    }


    protected boolean determineBlockingFromV1Property(int whenExhaustedAction)
    {
        ImmutableMap<Integer, Boolean> oldToNewExhaustedActionMapping = ImmutableMap.of(OBJ_POOL_V1_WHEN_EXHAUSTED_FAIL,
                        Boolean.valueOf(false), OBJ_POOL_V1_WHEN_EXHAUSTED_BLOCK,
                        Boolean.valueOf(true), OBJ_POOL_V1_WHEN_EXHAUSTED_GROW,
                        Boolean.valueOf(true));
        return ((Boolean)oldToNewExhaustedActionMapping.get(Integer.valueOf(whenExhaustedAction))).booleanValue();
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected GenericObjectPool.Config createPoolConfig()
    {
        GenericObjectPool.Config poolCfg = new GenericObjectPool.Config();
        if(getJNDIName() == null)
        {
            ConfigIntf cfg = getTenant().getConfig();
            poolCfg.maxActive = cfg.getInt("db.pool.maxActive", 20);
            poolCfg.maxIdle = cfg.getInt("db.pool.maxIdle", 20);
            poolCfg.minIdle = cfg.getInt("db.pool.minIdle", 1);
            poolCfg.timeBetweenEvictionRunsMillis = cfg.getInt("db.pool.timeBetweenEvictionRunsMillis", 10000);
            poolCfg.minEvictableIdleTimeMillis = cfg.getInt("db.pool.minEvictableIdleTimeMillis", 60000);
            poolCfg.testWhileIdle = cfg.getBoolean("db.pool.testWhileIdle", true);
            poolCfg.numTestsPerEvictionRun = cfg.getInt("db.pool.numTestsPerEvictionRun", 1000);
            poolCfg.testOnBorrow = cfg.getBoolean("db.pool.testOnBorrow", true);
            poolCfg.testOnReturn = cfg.getBoolean("db.pool.testOnReturn", true);
            poolCfg.whenExhaustedAction = (byte)cfg.getInt("db.pool.whenExhaustedAction", 1);
            poolCfg.maxWait = cfg.getInt("db.pool.maxWait", 10000);
        }
        else
        {
            poolCfg.maxActive = 1;
            poolCfg.maxIdle = 0;
            poolCfg.minIdle = 0;
            poolCfg.testOnBorrow = false;
            poolCfg.testWhileIdle = false;
            poolCfg.testOnReturn = false;
            poolCfg.whenExhaustedAction = 2;
        }
        return poolCfg;
    }


    protected GenericObjectPoolConfig createNewPoolConfig()
    {
        GenericObjectPoolConfig poolCfg = new GenericObjectPoolConfig();
        ConfigIntf cfg = getTenant().getConfig();
        poolCfg.setMaxTotal(cfg.getInt("db.pool.maxActive", 20));
        poolCfg.setMaxIdle(cfg.getInt("db.pool.maxIdle", 20));
        poolCfg.setMinIdle(cfg.getInt("db.pool.minIdle", 1));
        poolCfg.setTimeBetweenEvictionRunsMillis(cfg.getInt("db.pool.timeBetweenEvictionRunsMillis", 10000));
        poolCfg.setMinEvictableIdleTimeMillis(cfg.getInt("db.pool.minEvictableIdleTimeMillis", 60000));
        poolCfg.setTestWhileIdle(cfg.getBoolean("db.pool.testWhileIdle", true));
        poolCfg.setNumTestsPerEvictionRun(cfg.getInt("db.pool.numTestsPerEvictionRun", 1000));
        poolCfg.setTestOnBorrow(true);
        poolCfg.setTestOnReturn(true);
        boolean blockWhenExhausted = determineBlockingFromV1Property(cfg
                        .getInt("db.pool.whenExhaustedAction", OBJ_POOL_V1_WHEN_EXHAUSTED_BLOCK.intValue()));
        poolCfg.setBlockWhenExhausted(blockWhenExhausted);
        poolCfg.setMaxWaitMillis(cfg.getInt("db.pool.maxWait", 10000));
        poolCfg.setJmxNamePrefix(getTenant().getTenantID() + "-" + getTenant().getTenantID());
        return poolCfg;
    }


    private int calculateMaxPreparedStatementCount()
    {
        String dbName = this.info.databaseName;
        if("oracle".equalsIgnoreCase(dbName))
        {
            return 1000;
        }
        if("sqlserver".equalsIgnoreCase(dbName))
        {
            return 2000;
        }
        if("sap".equalsIgnoreCase(dbName))
        {
            return 32000;
        }
        if("postgresql".equalsIgnoreCase(dbName))
        {
            return 32000;
        }
        return -1;
    }


    public Map<String, String> getConnectionParameters()
    {
        return (this.connectionParameters != null) ? this.connectionParameters : Collections.EMPTY_MAP;
    }


    public String getJNDIName()
    {
        return this.jndiName;
    }


    public String toString()
    {
        return "DataSourceImpl<<" + this.tenant.getTenantID() + "/" + getID() + ">>";
    }


    public String getID()
    {
        return this.id;
    }


    public JDBCConnectionPool getConnectionPool()
    {
        return this.connectionPool;
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public Connection getConnection() throws SQLException
    {
        return getConnection(true);
    }


    public Connection getConnection(boolean transactionBound) throws SQLException
    {
        AbstractTenant tenant = (AbstractTenant)getTenant();
        if(!tenant.isStarting() && !tenant.isStopping() && !tenant.isNotifiyingListeners() && tenant.connectionHasBeenBroken())
        {
            Connection con = null;
            try
            {
                con = doGetConnection(transactionBound);
            }
            finally
            {
                if(con != null)
                {
                    try
                    {
                        con.close();
                    }
                    catch(SQLException e)
                    {
                        LOG.trace(e);
                    }
                }
            }
            Registry.unsetCurrentTenant();
            Registry.setCurrentTenant(getTenant());
            return getTenant().getDataSource().getConnection(transactionBound);
        }
        return doGetConnection(transactionBound);
    }


    protected Connection doGetConnection(boolean transactionBound) throws SQLException
    {
        long start = System.currentTimeMillis();
        try
        {
            Connection conn = getFromPool(transactionBound);
            this.getCounter.incrementAndGet();
            return conn;
        }
        finally
        {
            this.millisWaitedForConnection += System.currentTimeMillis() - start;
        }
    }


    private final Connection getFromPool(boolean transactionBound) throws SQLException
    {
        if(getConnectionPool().isPoolClosed())
        {
            throw new SQLException("ConnectionPool is already closed. cannot access database.");
        }
        Transaction tx = transactionBound ? Transaction.current() : null;
        if(tx != null && tx.isRunning())
        {
            return assertNotNull((Connection)tx.getTXBoundConnection(), tx);
        }
        try
        {
            return assertNotNull(getConnectionPool().borrowConnection(), tx);
        }
        catch(SQLException | RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private final Connection assertNotNull(Connection conn, Transaction tx)
    {
        if(conn == null)
        {
            (new Exception()).printStackTrace();
            throw new Error("connection was NULL, this should NEVER occur. (txbound: " + (
                            (tx != null) ? String.valueOf(tx.isRunning()) : "false") + ")");
        }
        return conn;
    }


    public Connection getConnection(String username, String password) throws SQLException
    {
        LOG.warn("getConnection( String user, String password ) called, but user and password are ignored for connections made with DataSourceImpl.");
        return getConnection();
    }


    public void returnToPool(ConnectionImpl conn)
    {
        if(!autoInvalidate(conn))
        {
            getConnectionPool().returnConnection((Connection)conn);
        }
    }


    private boolean autoInvalidate(ConnectionImpl conn)
    {
        if(isCloseConnetionOnRollbackAfterError() && conn.hasBeenRollbacked() && conn.gotError())
        {
            invalidate(conn);
            return true;
        }
        return false;
    }


    public void invalidate(ConnectionImpl conn)
    {
        getConnectionPool().invalidateConnection((Connection)conn);
    }


    public void destroy()
    {
        try
        {
            this.logUtils.destroy();
        }
        finally
        {
            getConnectionPool().close();
        }
    }


    public int getNumInUse()
    {
        return getConnectionPool().getNumActive();
    }


    public int getNumPhysicalOpen()
    {
        return getConnectionPool().getNumPhysicalOpen();
    }


    public int getNumReadOnlyOpen()
    {
        return getConnectionPool().getNumReadOnlyOpen();
    }


    public int getMaxInUse()
    {
        return getMaxPhysicalOpen();
    }


    public int getMaxPhysicalOpen()
    {
        return getConnectionPool().getMaxPhysicalOpen();
    }


    public long totalGets()
    {
        return this.getCounter.get();
    }


    public int getMaxAllowedPhysicalOpen()
    {
        return getConnectionPool().getMaxActive();
    }


    public long getMillisWaitedForConnection()
    {
        return this.millisWaitedForConnection;
    }


    public void resetStats()
    {
        this.millisWaitedForConnection = 0L;
        this.getCounter.set(0L);
        getConnectionPool().resetStats();
    }


    public JDBCLogUtils getLogUtils()
    {
        return this.logUtils;
    }


    public String getDatabaseName()
    {
        return this.info.databaseName;
    }


    public int getMaxPreparedParameterCount()
    {
        return this.maxPreparedParameterCount;
    }


    private static DataSourceInfo adjustDatabaseInfos(Tenant tenant, JDBCConnectionPool pool, DataSourceInfo previousInfo, String jndiName, Map<String, String> connectionParameters, boolean readOnly)
    {
        Connection conn = null;
        try
        {
            conn = pool.borrowConnection();
            DatabaseMetaData dmd = conn.getMetaData();
            String databaseURL = dmd.getURL();
            String databaseName = DatabaseNameResolver.guessDatabaseNameFromURL(databaseURL);
            String databaseVersion = dmd.getDatabaseProductVersion();
            String driverVersion = dmd.getDriverName() + " : " + dmd.getDriverName();
            String databaseUser = getUserNameFromDatabaseMetaData(dmd, tenant.getConfig().getParameter("db.username"));
            return new DataSourceInfo(databaseName, previousInfo.customsessionsql, databaseURL, databaseUser, previousInfo.databaseTablePrefix, previousInfo.databaseSchemaName, databaseVersion, driverVersion);
        }
        catch(Exception e)
        {
            LOG.error("error connecting to DataSource having " + (
                            (jndiName == null) ? ("url " + (String)connectionParameters.get(Config.SystemSpecificParams.DB_URL)) : ("jndi " +
                                            jndiName)) + ": " + e
                            .getMessage(), e);
            if(!readOnly)
            {
                throw new RuntimeException(e);
            }
            return previousInfo;
        }
        finally
        {
            if(conn != null)
            {
                pool.returnConnection(conn);
            }
        }
    }


    public static String getUserNameFromDatabaseMetaData(DatabaseMetaData dmd, String fallback)
    {
        String databaseUser;
        try
        {
            databaseUser = dmd.getUserName();
        }
        catch(SQLException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("could not call DatabaseMetaData.getUserName(), using configured 'db.username=" + fallback + "'", e);
            }
            databaseUser = fallback;
        }
        return databaseUser;
    }


    public String getDatabaseVersion()
    {
        return this.info.databaseVersion;
    }


    public String getDatabaseURL()
    {
        return this.info.databaseURL;
    }


    public String getCustomSessionSQL()
    {
        return this.info.customsessionsql;
    }


    public String getDatabaseUser()
    {
        return this.info.databaseUser;
    }


    private void closeNoError(Connection conn, Statement stmt, ResultSet resultSet)
    {
        if(resultSet != null)
        {
            try
            {
                resultSet.close();
            }
            catch(SQLException e)
            {
                LOG.debug(e);
            }
        }
        if(stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch(SQLException e)
            {
                LOG.debug(e);
            }
        }
        if(conn != null)
        {
            try
            {
                conn.close();
            }
            catch(SQLException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
    }


    public String getSchemaName()
    {
        if(this.info.databaseSchemaName == null)
        {
            synchronized(this)
            {
                if(this.info.databaseSchemaName == null)
                {
                    this.info = this.info.newSchemaName(readSchemaName());
                }
            }
        }
        return this.info.databaseSchemaName;
    }


    protected String readSchemaName()
    {
        String ret = null;
        if(getDatabaseName().equals("oracle"))
        {
            ret = readSchemaOracle();
        }
        else if(getDatabaseName().equals("sqlserver"))
        {
            ret = readSchemaSQLServer();
        }
        return ret;
    }


    private final String readSchemaSQLServer()
    {
        String ret = null;
        Connection conn = null;
        try
        {
            Statement stmt = null;
            ResultSet resultSet = null;
            try
            {
                conn = getConnection();
                stmt = conn.createStatement();
                resultSet = stmt.executeQuery("SELECT SCHEMA_NAME() AS schema_name ");
                while(resultSet.next())
                {
                    ret = resultSet.getString("schema_name");
                }
                if(ret == null)
                {
                    throw new RuntimeException("unable to find schema.");
                }
            }
            catch(Exception e)
            {
                LOG.info(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            finally
            {
                closeNoError(null, stmt, resultSet);
            }
        }
        finally
        {
            closeNoError(conn, null, null);
        }
        return ret;
    }


    private final String readSchemaOracle()
    {
        String ret = null;
        Connection conn = null;
        try
        {
            conn = getConnection();
            ret = conn.getMetaData().getUserName().toUpperCase();
        }
        catch(Exception e)
        {
            LOG.info(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        finally
        {
            closeNoError(conn, null, null);
        }
        return ret;
    }


    public boolean cannotConnect()
    {
        try
        {
            return getConnectionPool().cannotConnect();
        }
        catch(IllegalStateException e)
        {
            LOG.debug(e);
            return true;
        }
    }


    public DataSourceFactory getDataSourceFactory()
    {
        return this.factory;
    }


    public boolean isDBLogActive()
    {
        return getTenant().getConfig().getBoolean("db.log.active", false);
    }


    public boolean isDBLogAppendStackTraceActive()
    {
        return getTenant().getConfig().getBoolean("db.log.appendStackTrace", false);
    }


    public void setDBLog(boolean active)
    {
        getTenant().getConfig().setParameter("db.log.active", Boolean.toString(active));
    }


    public void setDBLogAppendStackTrace(boolean active)
    {
        getTenant().getConfig().setParameter("db.log.appendStackTrace", Boolean.toString(active));
    }


    public boolean isReadOnly()
    {
        return this.readOnly;
    }


    public String getTablePrefix()
    {
        return this.info.databaseTablePrefix;
    }


    public PrintWriter getLogWriter() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setLogWriter(PrintWriter arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public int getLoginTimeout() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setLoginTimeout(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isWrapperFor(Class<?> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T unwrap(Class<T> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new UnsupportedOperationException();
    }


    public DataAccessException translateToDataAccessException(SQLException e)
    {
        return getExceptionTranslator().translate("query", "", e);
    }


    public JDBCInterceptor getJDBCInterceptor()
    {
        return this.jdbcInterceptor;
    }


    private SQLExceptionTranslator getExceptionTranslator()
    {
        if(this.exceptionTranslator == null)
        {
            this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator((DataSource)this);
        }
        return (SQLExceptionTranslator)this.exceptionTranslator;
    }
}
