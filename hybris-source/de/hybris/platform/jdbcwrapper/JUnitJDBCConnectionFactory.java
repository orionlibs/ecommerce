package de.hybris.platform.jdbcwrapper;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.pool2.PooledObject;

public class JUnitJDBCConnectionFactory extends ConnectionErrorCheckingJDBCConnectionFactory
{
    private final AtomicBoolean forceAllConnectionsFail = new AtomicBoolean(false);
    private final Map<Connection, Connection> forceValidationErrorConnections = new ConcurrentHashMap<>();
    private final ConnectionStatus connectionStatus;


    public JUnitJDBCConnectionFactory(HybrisDataSource dataSource, ConnectionStatus connectionStatus)
    {
        super(dataSource, connectionStatus);
        this.connectionStatus = connectionStatus;
    }


    public boolean validateObject(PooledObject<Connection> pooledConnection)
    {
        Connection connection = (Connection)pooledConnection.getObject();
        boolean result = (!this.forceAllConnectionsFail.get() && !this.forceValidationErrorConnections.containsKey(connection) && super.validateObject(pooledConnection));
        if(!result)
        {
            this.connectionStatus.notifyConnectionError();
        }
        return result;
    }


    public void setAllConnectionsFail(boolean allFail)
    {
        this.forceAllConnectionsFail.set(allFail);
    }


    public void addFailingConnection(Connection con)
    {
        this.forceValidationErrorConnections.put(con, con);
    }


    public void removeFailingConnection(Connection con)
    {
        this.forceValidationErrorConnections.remove(con);
    }


    public void removeAllFailingConnections()
    {
        this.forceValidationErrorConnections.clear();
    }
}
