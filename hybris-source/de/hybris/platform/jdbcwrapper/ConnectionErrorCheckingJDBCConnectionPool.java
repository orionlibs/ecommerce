package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

public class ConnectionErrorCheckingJDBCConnectionPool extends JDBCConnectionPool
{
    private static final Logger LOG = Logger.getLogger(ConnectionErrorCheckingJDBCConnectionPool.class);
    private final ConnectionStatus connectionStatus;
    private static final int DEFAULT_MAY_TEST_AGAIN = 10;
    private int connectRetries;


    public ConnectionErrorCheckingJDBCConnectionPool(JDBCConnectionFactory factory, GenericObjectPoolConfig cfg, ConnectionStatus connectionStatus)
    {
        super(factory, cfg);
        this.connectionStatus = connectionStatus;
    }


    public boolean cannotConnect()
    {
        boolean cannot = this.connectionStatus.hadError();
        if(!this.connectionStatus.wasConnectionTestedOnce() || (cannot && mayTestAgain(getMayTestAgain())))
        {
            Connection conn = null;
            try
            {
                conn = borrowConnection();
                cannot = false;
            }
            catch(JDBCConnectionPoolInterruptedException interrupt)
            {
                Thread.currentThread().interrupt();
                this.connectionStatus.resetError();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("JDBCConnectionPool.borrowConnection failed: " + interrupt, (Throwable)interrupt);
                }
                cannot = false;
            }
            catch(Throwable t)
            {
                cannot = this.connectionStatus.hadError();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("JDBCConnectionPool.borrowConnection failed: " + t, t);
                }
                if(t instanceof SystemIsSuspendedException)
                {
                    throw (SystemIsSuspendedException)t;
                }
            }
            finally
            {
                this.connectionStatus.notifyTestedConnectionOnce();
                Utilities.tryToCloseJDBC(conn, null, null);
            }
        }
        return cannot;
    }


    private int getMayTestAgain()
    {
        if(Registry.hasCurrentTenant())
        {
            return Config.getInt("db.connectionerrorcheckingjdbcconnectionpool.maytestagain", 10);
        }
        return 10;
    }


    public Connection borrowConnection() throws Exception
    {
        return borrowConnection(getConnectRetries());
    }


    protected Connection borrowConnection(int retries) throws Exception
    {
        if(retries <= 0)
        {
            return doBorrowConnection();
        }
        try
        {
            return doBorrowConnection();
        }
        catch(SQLException e)
        {
            LOG.warn("Failed to establish JDBC connection. " + retries + " retries left...", e);
            return borrowConnection(retries - 1);
        }
    }


    protected Connection doBorrowConnection() throws Exception
    {
        try
        {
            Connection ret = super.borrowConnection();
            this.connectionStatus.resetError();
            return ret;
        }
        catch(SystemIsSuspendedException e)
        {
            this.connectionStatus.logSystemIsSuspended();
            throw e;
        }
        catch(Exception e)
        {
            this.connectionStatus.logError();
            throw e;
        }
    }


    protected boolean mayTestAgain(int intervalSeconds)
    {
        return (System.currentTimeMillis() - this.connectionStatus.getLastErrorTime() > (intervalSeconds * 1000));
    }


    public ConnectionErrorCheckingJDBCConnectionPool setConnectRetries(int connectRetries)
    {
        this.connectRetries = Math.max(0, connectRetries);
        LOG.info("JDBC Connect Retries set to " + this.connectRetries + ".");
        return this;
    }


    public int getConnectRetries()
    {
        return this.connectRetries;
    }
}
