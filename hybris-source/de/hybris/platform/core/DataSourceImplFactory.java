package de.hybris.platform.core;

import de.hybris.platform.jdbcwrapper.ConnectionErrorCheckingJDBCConnectionFactory;
import de.hybris.platform.jdbcwrapper.ConnectionErrorCheckingJDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.ConnectionImpl;
import de.hybris.platform.jdbcwrapper.ConnectionStatus;
import de.hybris.platform.jdbcwrapper.DataSourceImpl;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.JDBCConnectionFactory;
import de.hybris.platform.jdbcwrapper.JDBCConnectionPool;
import de.hybris.platform.jdbcwrapper.PreparedStatementImpl;
import de.hybris.platform.jdbcwrapper.ResultSetImpl;
import de.hybris.platform.jdbcwrapper.StatementImpl;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DataSourceImplFactory implements DataSourceFactory
{
    public HybrisDataSource createJNDIDataSource(String id, Tenant tenant, String fromJNDI, boolean readOnly)
    {
        return (HybrisDataSource)new DataSourceImpl(tenant, id, fromJNDI, readOnly, this);
    }


    public HybrisDataSource createDataSource(String id, Tenant tenant, Map<String, String> connectionParams, boolean readOnly)
    {
        return (HybrisDataSource)new DataSourceImpl(tenant, id, connectionParams, readOnly, this);
    }


    public JDBCConnectionPool createConnectionPool(HybrisDataSource dataSource, GenericObjectPoolConfig poolConfig)
    {
        ConnectionErrorCheckingJDBCConnectionFactory connectionErrorCheckingJDBCConnectionFactory;
        JDBCConnectionPool pool;
        ConnectionStatus connectionStatus = new ConnectionStatus();
        try
        {
            connectionErrorCheckingJDBCConnectionFactory = new ConnectionErrorCheckingJDBCConnectionFactory(dataSource, connectionStatus);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        if(isDatabaseConnectionErrorDetectionEnabled(dataSource))
        {
            ConnectionErrorCheckingJDBCConnectionPool connectionErrorCheckingJDBCConnectionPool = (new ConnectionErrorCheckingJDBCConnectionPool((JDBCConnectionFactory)connectionErrorCheckingJDBCConnectionFactory, poolConfig, connectionStatus)).setConnectRetries(dataSource
                            .getTenant().getConfig().getInt("db.pool.connectRetries", 3));
        }
        else
        {
            pool = new JDBCConnectionPool((JDBCConnectionFactory)connectionErrorCheckingJDBCConnectionFactory, poolConfig);
        }
        pool.setDumpStackOnConnectionError(dataSource.getTenant().getConfig().getBoolean("db.pool.dumpThreadsOnBorrowError", true));
        return pool;
    }


    protected boolean isDatabaseConnectionErrorDetectionEnabled(HybrisDataSource dataSource)
    {
        return dataSource.getTenant().getConfig().getBoolean("db.detect.connection.errors", true);
    }


    public Connection wrapConnection(HybrisDataSource wrappedDataSource, Connection rawConnection)
    {
        JDBCInterceptor interceptor = wrappedDataSource.getJDBCInterceptor();
        Connection interceptedConnection = (Connection)interceptor.wrap(rawConnection, Connection.class);
        return (Connection)new ConnectionImpl(wrappedDataSource, interceptedConnection);
    }


    public PreparedStatement wrapPreparedStatement(Connection wrappedConnection, PreparedStatement rawStatement, String query)
    {
        ConnectionImpl connectionImpl = (ConnectionImpl)wrappedConnection;
        JDBCInterceptor interceptor = connectionImpl.getDataSource().getJDBCInterceptor();
        PreparedStatement interceptedPreparedStatement = (PreparedStatement)interceptor.wrap(rawStatement, PreparedStatement.class);
        return (PreparedStatement)new PreparedStatementImpl(connectionImpl, interceptedPreparedStatement, query);
    }


    public Statement wrapStatement(Connection wrappedConnection, Statement rawStatement)
    {
        ConnectionImpl connectionImpl = (ConnectionImpl)wrappedConnection;
        JDBCInterceptor interceptor = connectionImpl.getDataSource().getJDBCInterceptor();
        Statement interceptedPreparedStatement = (Statement)interceptor.wrap(rawStatement, Statement.class);
        return (Statement)new StatementImpl(connectionImpl, interceptedPreparedStatement);
    }


    public ResultSet wrapResultSet(Statement wrappedStatement, ResultSet rawResultSet)
    {
        JDBCInterceptor interceptor;
        StatementImpl statementImpl = (StatementImpl)wrappedStatement;
        try
        {
            interceptor = statementImpl.getConnection().getDataSource().getJDBCInterceptor();
        }
        catch(SQLException e)
        {
            throw new IllegalArgumentException("wrappedStatement must hold valid connection", e);
        }
        ResultSet interceptedResultSet = (ResultSet)interceptor.wrap(rawResultSet, ResultSet.class);
        return (ResultSet)new ResultSetImpl(statementImpl, interceptedResultSet);
    }
}
