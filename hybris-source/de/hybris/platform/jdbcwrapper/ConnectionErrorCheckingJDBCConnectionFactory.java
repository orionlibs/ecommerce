package de.hybris.platform.jdbcwrapper;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.pool2.PooledObject;

public class ConnectionErrorCheckingJDBCConnectionFactory extends JDBCConnectionFactory
{
    private final ConnectionStatus connectionStatus;


    public ConnectionErrorCheckingJDBCConnectionFactory(HybrisDataSource dataSource, ConnectionStatus connectionStatus)
    {
        super(dataSource);
        this.connectionStatus = connectionStatus;
    }


    protected Connection createConnection() throws SQLException
    {
        long errorCounter = this.connectionStatus.getErrorCounter();
        Connection connection = super.createConnection();
        if(connection instanceof ConnectionImpl)
        {
            ((ConnectionImpl)connection).setErrorHandler((message, exception) -> this.connectionStatus.notifyConnectionError());
            ((ConnectionImpl)connection).markValid(errorCounter);
        }
        return connection;
    }


    protected boolean mustValidate(Object obj)
    {
        return (((ConnectionImpl)obj).gotError() || ((ConnectionImpl)obj).getLastValidationErrorCounter() != this.connectionStatus.getErrorCounter());
    }


    public boolean validateObject(PooledObject<Connection> pooledConnection)
    {
        long errorCounter = this.connectionStatus.getErrorCounter();
        boolean validationOk = super.validateObject(pooledConnection);
        if(validationOk)
        {
            ConnectionImpl connection = (ConnectionImpl)pooledConnection.getObject();
            connection.resetError();
            connection.markValid(errorCounter);
        }
        else
        {
            this.connectionStatus.notifyConnectionError();
        }
        return validationOk;
    }
}
