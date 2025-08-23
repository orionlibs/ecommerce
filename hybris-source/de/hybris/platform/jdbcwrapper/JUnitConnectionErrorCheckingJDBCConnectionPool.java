package de.hybris.platform.jdbcwrapper;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class JUnitConnectionErrorCheckingJDBCConnectionPool extends ConnectionErrorCheckingJDBCConnectionPool implements JUnitJDBCConnectionPool
{
    private final JUnitConnectionStatus connectionStatus;
    private final JUnitJDBCConnectionFactory factory;
    private final AtomicBoolean suspendConnectionBorrowing = new AtomicBoolean(false);


    public JUnitConnectionErrorCheckingJDBCConnectionPool(JUnitJDBCConnectionFactory factory, GenericObjectPoolConfig cfg, JUnitConnectionStatus connectionStatus)
    {
        super((JDBCConnectionFactory)factory, cfg, (ConnectionStatus)connectionStatus);
        this.connectionStatus = connectionStatus;
        this.factory = factory;
    }


    public boolean mustValidate(Connection con)
    {
        return this.factory.mustValidate(con);
    }


    public void setPoolHasConnectionErrors(boolean hasErrors)
    {
        this.connectionStatus.setPoolHasConnectionErrors(hasErrors);
    }


    public void addFailingConnection(Connection con)
    {
        this.factory.addFailingConnection(con);
    }


    public void removeFailingConnection(Connection con)
    {
        this.factory.removeFailingConnection(con);
    }


    public void setAllConnectionsFail(boolean allFail)
    {
        this.factory.setAllConnectionsFail(allFail);
    }


    public void resetTestMode()
    {
        this.factory.setAllConnectionsFail(false);
        this.connectionStatus.setPoolHasConnectionErrors(false);
        this.factory.removeAllFailingConnections();
    }


    protected boolean mayTestAgain(int intervalSeconds)
    {
        return true;
    }


    public void resumeConnectionBorrowing()
    {
        this.suspendConnectionBorrowing.set(false);
    }


    public void suspendConnectionBorrowing()
    {
        this.suspendConnectionBorrowing.set(true);
    }


    public Connection borrowConnection() throws Exception
    {
        int counter = 0;
        while(this.suspendConnectionBorrowing.get() && counter < 25)
        {
            Thread.sleep(200L);
            counter++;
        }
        return super.borrowConnection();
    }


    public long getConnectionStatusErrorCounter()
    {
        return this.connectionStatus.getErrorCounter();
    }
}
