package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.DataSourceImplFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class JUnitDataSourceFactory extends DataSourceImplFactory
{
    public JDBCConnectionPool createConnectionPool(HybrisDataSource dataSource, GenericObjectPoolConfig poolConfig)
    {
        JUnitConnectionStatus connectionStatus = new JUnitConnectionStatus();
        JUnitJDBCConnectionFactory factory = new JUnitJDBCConnectionFactory(dataSource, (ConnectionStatus)connectionStatus);
        JUnitConnectionErrorCheckingJDBCConnectionPool jUnitConnectionErrorCheckingJDBCConnectionPool = new JUnitConnectionErrorCheckingJDBCConnectionPool(factory, poolConfig, connectionStatus);
        jUnitConnectionErrorCheckingJDBCConnectionPool.setDumpStackOnConnectionError(dataSource.getTenant().getConfig()
                        .getBoolean("db.pool.dumpThreadsOnBorrowError", true));
        return (JDBCConnectionPool)jUnitConnectionErrorCheckingJDBCConnectionPool;
    }


    public Connection wrapConnection(HybrisDataSource wrappedDataSource, Connection rawConnection)
    {
        return (Connection)new JUnitConnectionImpl(wrappedDataSource, rawConnection);
    }


    public PreparedStatement wrapPreparedStatement(Connection wrappedConnection, PreparedStatement rawStatement, String query)
    {
        return (PreparedStatement)new JUnitPreparedStatementImpl((ConnectionImpl)wrappedConnection, rawStatement, query);
    }
}
