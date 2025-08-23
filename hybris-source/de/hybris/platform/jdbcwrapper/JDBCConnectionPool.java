package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.governor.ExecutionContext;
import de.hybris.platform.governor.ExecutionInformation;
import de.hybris.platform.governor.ResourceGovernor;
import de.hybris.platform.governor.ResourceGovernorProvider;
import de.hybris.platform.util.ThreadUtilities;
import java.sql.Connection;
import java.util.Date;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

public class JDBCConnectionPool
{
    private static final Logger LOG = Logger.getLogger(JDBCConnectionPool.class);
    private final JDBCConnectionFactory factory;
    private final GenericObjectPoolConfig connectionPoolConfig;
    private final ObjectPool<Connection> connectionPool;
    private final JdbcSuspendSupport jdbcSuspendSupport;
    private boolean dumpStackOnConnectionError = true;
    private volatile boolean closed;


    public JDBCConnectionPool(JDBCConnectionFactory factory, GenericObjectPoolConfig cfg)
    {
        this.factory = factory;
        this.connectionPoolConfig = cfg;
        this.connectionPool = (ObjectPool<Connection>)new GenericObjectPool((PooledObjectFactory)factory, this.connectionPoolConfig);
        this.closed = false;
        this.jdbcSuspendSupport = factory.getJdbcSuspendSupport();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public JDBCConnectionPool(JDBCConnectionFactory factory, GenericObjectPool.Config cfg)
    {
        this.factory = factory;
        this.connectionPoolConfig = convertOldPoolConfigToV2(cfg);
        this.connectionPool = (ObjectPool<Connection>)new GenericObjectPool((PooledObjectFactory)factory, this.connectionPoolConfig);
        this.closed = false;
        this.jdbcSuspendSupport = factory.getJdbcSuspendSupport();
    }


    private GenericObjectPoolConfig convertOldPoolConfigToV2(GenericObjectPool.Config oldPoolConfig)
    {
        GenericObjectPoolConfig newPoolConfig = new GenericObjectPoolConfig();
        newPoolConfig.setMaxIdle(oldPoolConfig.maxIdle);
        newPoolConfig.setMinIdle(oldPoolConfig.minIdle);
        newPoolConfig.setMaxTotal(oldPoolConfig.maxActive);
        newPoolConfig.setMaxWaitMillis(oldPoolConfig.maxWait);
        return newPoolConfig;
    }


    public void setDumpStackOnConnectionError(boolean enable)
    {
        this.dumpStackOnConnectionError = enable;
    }


    public int getNumPhysicalOpen()
    {
        return this.factory.getNumPhysicalOpen();
    }


    public int getNumReadOnlyOpen()
    {
        return this.factory.getNumReadOnlyOpen();
    }


    public int getMaxPhysicalOpen()
    {
        return this.factory.getMaxPhysicalOpen();
    }


    protected void resetStats()
    {
        this.factory.resetStats();
    }


    public boolean cannotConnect()
    {
        return false;
    }


    public boolean isPoolClosed()
    {
        return this.closed;
    }


    public Connection borrowConnection() throws Exception
    {
        ResourceGovernor resourceGovernor = ResourceGovernorProvider.getInstance().getResourceGovernor();
        ExecutionInformation executionInformation = resourceGovernor.fromCurrentOperationInfo("JDBCConnectionPool.borrowConnection").build();
        try
        {
            ExecutionContext executionContext = resourceGovernor.beginExecution(executionInformation);
            try
            {
                this.jdbcSuspendSupport.aboutToBorrowTheConnection();
                Connection connection = (Connection)this.connectionPool.borrowObject();
                if(executionContext != null)
                {
                    executionContext.close();
                }
                return connection;
            }
            catch(Throwable throwable)
            {
                if(executionContext != null)
                {
                    try
                    {
                        executionContext.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new JDBCConnectionPoolInterruptedException(e);
        }
        catch(Exception e)
        {
            logErrorOnBorrow(e);
            throw e;
        }
    }


    private void logErrorOnBorrow(Exception exc)
    {
        if(exc instanceof java.util.NoSuchElementException || (exc
                        .getMessage() != null && exc.getMessage().toLowerCase().contains("timeout")))
        {
            String threadDump = this.dumpStackOnConnectionError ? ("Thread dump:\n" + ThreadUtilities.printThreadDump()) : "(enable 'db.pool.dumpThreadsOnConnectionError' to see complete thread dump)";
            LOG.error("----------------------------------------\n" + new Date() + ": problem getting database connection\nException message thrown was: " + exc
                            .getMessage() + "\n" + threadDump + "\n----------------------------------------");
        }
    }


    public void returnConnection(Connection conn)
    {
        try
        {
            this.connectionPool.returnObject(conn);
        }
        catch(Exception e)
        {
            LOG.error("error returning JDBC connection " + conn, e);
        }
    }


    public void invalidateConnection(Connection conn)
    {
        try
        {
            this.connectionPool.invalidateObject(conn);
        }
        catch(Exception e)
        {
            LOG.warn("error invalidating connection", e);
        }
    }


    public final void close()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Closing " + this);
        }
        try
        {
            this.closed = true;
            this.connectionPool.close();
        }
        catch(Exception e)
        {
            LOG.error("Error closing JDBC connection pool", e);
        }
    }


    public String toString()
    {
        return "JDBCConnectionPool(factory:" + this.factory + ")";
    }


    public int getNumActive()
    {
        return this.connectionPool.getNumActive();
    }


    public int getMaxActive()
    {
        return this.connectionPoolConfig.getMaxTotal();
    }


    public int getNumIdle()
    {
        return this.connectionPool.getNumIdle();
    }


    public void enableOracleStatementCaching(int cacheSize)
    {
        this.factory.enableOracleStatementCaching(cacheSize);
    }


    public void resetOracleStatementCaching()
    {
        this.factory.resetOracleStatementCaching();
    }


    public void disableOracleStatementCaching()
    {
        this.factory.disableOracleStatementCaching();
    }
}
