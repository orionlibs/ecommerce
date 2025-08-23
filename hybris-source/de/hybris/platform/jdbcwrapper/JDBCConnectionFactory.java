package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.threadregistry.SuspendResumeServices;
import de.hybris.platform.jdbcwrapper.interceptor.recover.RecoveryInProgressException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.JdbcUtils;

public class JDBCConnectionFactory implements PooledObjectFactory<Connection>
{
    private static final Logger LOG = Logger.getLogger(JDBCConnectionFactory.class);
    private final HybrisDataSource dataSource;
    private final AtomicInteger currentReadOnlyPhysical = new AtomicInteger(0);
    private final AtomicInteger currentPhysical = new AtomicInteger(0);
    private final AtomicInteger maxPhysical = new AtomicInteger(0);
    private boolean warnedAboutTrxCommitSetting = false;
    private final DataSource rawDataSource;
    private final OracleStatementCachingConfigurer oracleHelper;
    private final AzureConnectionFactoryHelper azureHelper;
    private volatile String connectionValidationQuery;


    public void resetStats()
    {
        this.maxPhysical.set(0);
    }


    public int getNumPhysicalOpen()
    {
        return this.currentPhysical.get();
    }


    public int getMaxPhysicalOpen()
    {
        return this.maxPhysical.get();
    }


    public int getNumReadOnlyOpen()
    {
        return this.currentReadOnlyPhysical.get();
    }


    public String toString()
    {
        return (this.dataSource.getJNDIName() == null) ? ("JDBCConnectionFactory<<" +
                        getSystemID() + ">>[" + getConfiguredUser() + "," + getConfiguredUrl() + "," +
                        getConfiguredDriver() + ",prefix:" + this.dataSource.getTablePrefix() + "]") : ("JDBCConnectionFactory<<" +
                        getSystemID() + ">>[jndi=" + this.dataSource.getJNDIName() + "]");
    }


    public PooledObject<Connection> makeObject() throws SQLException
    {
        return (PooledObject<Connection>)new DefaultPooledObject(createConnection());
    }


    protected Connection createConnection() throws SQLException
    {
        Connection connection = createSQLConnection();
        try
        {
            connection = this.dataSource.getDataSourceFactory().wrapConnection(this.dataSource, connection);
            this.azureHelper.applyAzureReadOnlySettings(connection);
            if(connection.isReadOnly())
            {
                this.currentReadOnlyPhysical.incrementAndGet();
            }
        }
        catch(SQLException | RuntimeException e)
        {
            closeQuietly(connection);
            throw e;
        }
        return connection;
    }


    private DataSource createRawDataSource()
    {
        Driver driver;
        try
        {
            driver = (Driver)Class.forName(getConfiguredDriver()).newInstance();
        }
        catch(Exception e)
        {
            LOG.error("The required db driver (" + getConfiguredDriver() + ") couldn't be loaded. Please make sure it exists in your classpath.", e);
            throw new IllegalStateException("Can not load driver \"" + getConfiguredDriver() + "\" due to " + e.getMessage(), e);
        }
        Properties connectionParams = collectAndAdjustConnectionParamsFromDataSource();
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource(driver, getConfiguredUrl(), connectionParams);
        return (DataSource)this.dataSource.getJDBCInterceptor().wrap(new LoggingDataSource((DataSource)simpleDriverDataSource), DataSource.class);
    }


    private final Properties collectAndAdjustConnectionParamsFromDataSource()
    {
        Properties parameters = new Properties();
        parameters.put("user", readUserParam());
        parameters.put("password", readPasswordParam());
        appendSpecialConnectionParamaters(parameters);
        appendCustomConnectionParamaters(parameters);
        return parameters;
    }


    private void appendCustomConnectionParamaters(Properties parameters)
    {
        Map<String, String> params = AbstractTenant.extractCustomDBParams(this.dataSource.getConnectionParameters(), true);
        parameters.putAll(params);
    }


    private void appendSpecialConnectionParamaters(Properties parameters)
    {
        if("oracle".equals(this.dataSource.getDatabaseName()))
        {
            parameters.put("SetBigStringTryClob", "true");
            parameters.put("oracle.jdbc.TcpNoDelay", "true");
        }
    }


    private String readPasswordParam()
    {
        String pwd = getConfiguredPassword();
        return (pwd != null) ? pwd : "";
    }


    private String readUserParam()
    {
        String user = getConfiguredUser();
        if(user == null && "hsqldb".equals(this.dataSource.getDatabaseName()))
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("############## XXX: configured hsql user was null!!!");
            }
            user = "sa";
        }
        return user;
    }


    protected Connection createRawSQLConnection() throws SQLException
    {
        Connection rawConnection = getConfiguredDataSource().getConnection();
        if(rawConnection == null)
        {
            throw new SQLException("Configured datasource returned 'null' - no connection");
        }
        return rawConnection;
    }


    private DataSource getConfiguredDataSource() throws SQLException
    {
        if(this.rawDataSource != null)
        {
            return this.rawDataSource;
        }
        try
        {
            DataSource dsFromJNDI = (DataSource)(new InitialContext()).lookup(this.dataSource.getJNDIName());
            return (DataSource)this.dataSource.getJDBCInterceptor().wrap(new LoggingDataSource(dsFromJNDI), DataSource.class);
        }
        catch(NamingException e)
        {
            throw new SQLException("error looking up JNDI data source '" + this.dataSource
                            .getJNDIName() + "' due to " + e.getMessage(), e);
        }
    }


    private Connection createSQLConnection() throws SQLException
    {
        Connection sqlConnection = null;
        try
        {
            sqlConnection = createRawSQLConnection();
            if(sqlConnection == null)
            {
                throw new SQLException("could not create connection for '" + getConfiguredDriver() + "://" + getConfiguredUser() + "@" +
                                getConfiguredUrl() + "'");
            }
            increaseConnectionCounter();
            checkAndWarnAboutWrongMySQLSettings(sqlConnection);
            applyCustomSessionSQL(sqlConnection);
            applyOracleStatementCachingSettings(sqlConnection);
            return sqlConnection;
        }
        catch(SQLException e)
        {
            closeQuietly(sqlConnection);
            throw e;
        }
        catch(Exception e)
        {
            closeQuietly(sqlConnection);
            throw new SQLException(e);
        }
    }


    private void closeQuietly(Connection connection)
    {
        if(connection == null)
        {
            return;
        }
        try
        {
            connection.close();
        }
        catch(SQLException e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    private final int increaseConnectionCounter()
    {
        int newNumber = this.currentPhysical.incrementAndGet();
        int currentMax = this.maxPhysical.get();
        while(newNumber > currentMax && !this.maxPhysical.compareAndSet(currentMax, newNumber))
        {
            currentMax = this.maxPhysical.get();
        }
        return newNumber;
    }


    private final void applyCustomSessionSQL(Connection sqlConnection)
    {
        String customSQL = this.dataSource.getCustomSessionSQL();
        if(customSQL != null)
        {
            StringTokenizer toks = new StringTokenizer(customSQL, ";\n");
            while(toks.hasMoreTokens())
            {
                String sql = toks.nextToken();
                if(StringUtils.isNotBlank(sql))
                {
                    Statement stmt = null;
                    try
                    {
                        stmt = sqlConnection.createStatement();
                        stmt.executeUpdate(sql);
                    }
                    catch(SQLException e)
                    {
                        LOG.warn("error while executing custom session sql (property db.customsessionsql): '" + sql + "': ", e);
                    }
                    finally
                    {
                        Utilities.tryToCloseJDBC(null, stmt, null);
                    }
                }
            }
        }
    }


    private final void checkAndWarnAboutWrongMySQLSettings(Connection sqlConnection)
    {
        if("mysql".equals(this.dataSource.getDatabaseName()))
        {
            if(!this.warnedAboutTrxCommitSetting)
            {
                Statement stmt = null;
                ResultSet resultSet = null;
                try
                {
                    stmt = sqlConnection.createStatement();
                    resultSet = stmt.executeQuery("select variable_value from INFORMATION_SCHEMA.GLOBAL_VARIABLES where variable_name='innodb_flush_log_at_trx_commit';");
                    resultSet.next();
                    String valueS = resultSet.getString(1);
                    int value = Integer.parseInt(valueS);
                    if(value != 0)
                    {
                        LOG.warn("**********");
                        LOG.warn("POTENTIAL PERFORMANCE RISK: You are using MySQL and have not set the 'innodb_flush_log_at_trx_commit' to 0 (write to log and flush every second).");
                        LOG.warn("We STRONGLY recommend to set this global MySQL variable to 0. (using command line or MySQL Administrator)");
                        LOG.warn("Please ask support@hybris.de if you have further questions.");
                        LOG.warn("If you keep on running with current configuration you may encounter a very slow system if you are working with InnoDB tables.");
                        LOG.warn("**********");
                    }
                }
                catch(SQLException e)
                {
                    LOG.trace(e);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("********");
                        LOG.debug("MySQL: cannot determine if 'innodb_flush_log_at_trx_commit' is set correctly. please check if this global mysql variable is set to '0' (write to log and flush every second).");
                        LOG.debug("Error during reading global variables: " + e.getMessage());
                        LOG.debug("********");
                    }
                }
                finally
                {
                    Utilities.tryToCloseJDBC(null, stmt, resultSet);
                    this.warnedAboutTrxCommitSetting = true;
                }
            }
        }
    }


    public void activateObject(PooledObject<Connection> pooledConnection)
    {
        ConnectionImpl connection = (ConnectionImpl)pooledConnection.getObject();
        connection.prepareForUse();
    }


    public void passivateObject(PooledObject<Connection> pooledConnection)
    {
        ConnectionImpl connection = (ConnectionImpl)pooledConnection.getObject();
        try
        {
            connection.restoreAfterUse();
        }
        catch(SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public void destroyObject(PooledObject<Connection> pooledConnection) throws SQLException
    {
        Object connection = pooledConnection.getObject();
        try
        {
            ConnectionImpl connImpl = (ConnectionImpl)connection;
            if(connImpl.isReadOnly())
            {
                this.currentReadOnlyPhysical.decrementAndGet();
            }
            connImpl.destroy();
        }
        catch(RecoveryInProgressException e)
        {
            LOG.debug("Destroying failed because of the recovery being in progress.", (Throwable)e);
        }
        catch(SQLException e)
        {
            LOG.trace(e);
            LOG.info("Error while destroying connection " + connection + " : " + e.getMessage());
        }
        finally
        {
            int remaining = this.currentPhysical.decrementAndGet();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Destroyed JDBC connection ( remaining" + remaining + ")");
            }
        }
    }


    protected boolean mustValidate(Object obj)
    {
        return false;
    }


    public JDBCConnectionFactory(HybrisDataSource dateSource)
    {
        this.connectionValidationQuery = null;
        this.dataSource = dateSource;
        this.oracleHelper = new OracleStatementCachingConfigurer(this.dataSource);
        this.azureHelper = new AzureConnectionFactoryHelper(this.dataSource);
        if(this.dataSource.getJNDIName() == null)
        {
            this.rawDataSource = createRawDataSource();
        }
        else
        {
            this.rawDataSource = null;
        }
    }


    protected String getValidationQuery()
    {
        if(this.connectionValidationQuery == null)
        {
            String dbName = this.dataSource.getDatabaseName();
            if(dbName != null)
            {
                if("oracle".equalsIgnoreCase(dbName))
                {
                    this.connectionValidationQuery = "SELECT 1 FROM dual";
                }
                else if("mysql".equalsIgnoreCase(dbName) || "sqlserver".equalsIgnoreCase(dbName))
                {
                    this.connectionValidationQuery = "SELECT 1";
                }
                else if("hsqldb".equalsIgnoreCase(dbName))
                {
                    this.connectionValidationQuery = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS";
                }
                else if("sap".equalsIgnoreCase(dbName))
                {
                    this.connectionValidationQuery = "SELECT 1 from dummy";
                }
                else
                {
                    this.connectionValidationQuery = "";
                }
            }
        }
        return this.connectionValidationQuery;
    }


    public boolean validateObject(PooledObject<Connection> pooledConnection)
    {
        ConnectionImpl connection = (ConnectionImpl)pooledConnection.getObject();
        boolean success = true;
        if(mustValidate(connection))
        {
            try
            {
                if(!isDatabaseRunningInSameProcess())
                {
                    String stdQuery = getValidationQuery();
                    Connection rawConn = connection.getUnderlayingConnection();
                    success = StringUtils.isNotBlank(stdQuery) ? validateUsingStandardQuery(rawConn, stdQuery) : validateUsingOwnTable(rawConn);
                }
                else
                {
                    success = (connection.getUnderlayingConnection() != null);
                }
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
                success = false;
            }
        }
        return success;
    }


    private boolean validateUsingOwnTable(Connection conn)
    {
        boolean success = true;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeQuery("SELECT testcol FROM connpooltest");
        }
        catch(Exception e)
        {
            LOG.trace(e);
            Statement stmt2 = null;
            try
            {
                stmt2 = conn.createStatement();
                stmt2.executeUpdate("CREATE TABLE connpooltest (testcol integer)");
            }
            catch(Exception e2)
            {
                LOG.trace(e2);
                success = false;
            }
            finally
            {
                JdbcUtils.closeStatement(stmt2);
            }
        }
        finally
        {
            JdbcUtils.closeStatement(stmt);
        }
        return success;
    }


    private boolean validateUsingStandardQuery(Connection conn, String query)
    {
        boolean success = true;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeQuery(query);
        }
        catch(Exception e)
        {
            LOG.trace(e);
            success = false;
        }
        finally
        {
            JdbcUtils.closeStatement(stmt);
        }
        return success;
    }


    boolean isDatabaseRunningInSameProcess()
    {
        return (this.dataSource == null || ("hsqldb"
                        .equals(this.dataSource.getDatabaseName()) && !getConfiguredUrl().contains("hsql://")));
    }


    private String getConfiguredDriver()
    {
        return (String)this.dataSource.getConnectionParameters().get(Config.SystemSpecificParams.DB_DRIVER);
    }


    private String getConfiguredPassword()
    {
        return (String)this.dataSource.getConnectionParameters().get(Config.SystemSpecificParams.DB_PASSWORD);
    }


    private String getSystemID()
    {
        return this.dataSource.getTenant().getTenantID();
    }


    private String getConfiguredUrl()
    {
        return (String)this.dataSource.getConnectionParameters().get(Config.SystemSpecificParams.DB_URL);
    }


    private String getConfiguredUser()
    {
        return (String)this.dataSource.getConnectionParameters().get(Config.SystemSpecificParams.DB_USERNAME);
    }


    public void enableOracleStatementCaching(int cacheSize)
    {
        this.oracleHelper.enableOracleStatementCaching(cacheSize);
    }


    public void resetOracleStatementCaching()
    {
        this.oracleHelper.resetOracleStatementCaching();
    }


    public void disableOracleStatementCaching()
    {
        this.oracleHelper.disableOracleStatementCaching();
    }


    public boolean isOracleStatementCachingEnabled()
    {
        return this.oracleHelper.isOracleStatementCachingEnabled();
    }


    private void applyOracleStatementCachingSettings(Connection conn)
    {
        this.oracleHelper.applyOracleStatementCachingSettings(conn);
    }


    protected JdbcSuspendSupport getJdbcSuspendSupport()
    {
        return SuspendResumeServices.getInstance().getJdbcSuspendSupport();
    }
}
