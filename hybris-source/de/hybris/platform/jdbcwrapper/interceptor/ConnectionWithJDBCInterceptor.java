package de.hybris.platform.jdbcwrapper.interceptor;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;

class ConnectionWithJDBCInterceptor implements Connection
{
    private final Connection target;
    private final JDBCInterceptor jdbcInterceptor;


    public ConnectionWithJDBCInterceptor(Connection target, JDBCInterceptor jdbcInterceptor)
    {
        this.target = Objects.<Connection>requireNonNull(target, "target mustn't be null.");
        this.jdbcInterceptor = Objects.<JDBCInterceptor>requireNonNull(jdbcInterceptor, "jdbcInterceptor mustn't be null.");
    }


    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        if(iface.isAssignableFrom(getClass()))
        {
            return iface.cast(this);
        }
        if(iface.isAssignableFrom(this.target.getClass()))
        {
            return iface.cast(this.target);
        }
        return (T)this.jdbcInterceptor.get(() -> this.target.unwrap(iface));
    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return (iface.isAssignableFrom(getClass()) || ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isWrapperFor(iface)))).booleanValue());
    }


    public Statement createStatement() throws SQLException
    {
        return (Statement)this.jdbcInterceptor.get(() -> this.target.createStatement());
    }


    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql));
    }


    public CallableStatement prepareCall(String sql) throws SQLException
    {
        return (CallableStatement)this.jdbcInterceptor.get(() -> this.target.prepareCall(sql));
    }


    public String nativeSQL(String sql) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.nativeSQL(sql));
    }


    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setAutoCommit(autoCommit));
    }


    public boolean getAutoCommit() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getAutoCommit()))).booleanValue();
    }


    public void commit() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.commit());
    }


    public void rollback() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.rollback());
    }


    public void close() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.close());
    }


    public boolean isClosed() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isClosed()))).booleanValue();
    }


    public DatabaseMetaData getMetaData() throws SQLException
    {
        return (DatabaseMetaData)this.jdbcInterceptor.get(() -> this.target.getMetaData());
    }


    public void setReadOnly(boolean readOnly) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setReadOnly(readOnly));
    }


    public boolean isReadOnly() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isReadOnly()))).booleanValue();
    }


    public void setCatalog(String catalog) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCatalog(catalog));
    }


    public String getCatalog() throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getCatalog());
    }


    public void setTransactionIsolation(int level) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTransactionIsolation(level));
    }


    public int getTransactionIsolation() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getTransactionIsolation()))).intValue();
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return (SQLWarning)this.jdbcInterceptor.get(() -> this.target.getWarnings());
    }


    public void clearWarnings() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearWarnings());
    }


    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return (Statement)this.jdbcInterceptor.get(() -> this.target.createStatement(resultSetType, resultSetConcurrency));
    }


    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql, resultSetType, resultSetConcurrency));
    }


    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        return (CallableStatement)this.jdbcInterceptor.get(() -> this.target.prepareCall(sql, resultSetType, resultSetConcurrency));
    }


    public Map<String, Class<?>> getTypeMap() throws SQLException
    {
        return (Map<String, Class<?>>)this.jdbcInterceptor.get(() -> this.target.getTypeMap());
    }


    public void setTypeMap(Map<String, Class<?>> map) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTypeMap(map));
    }


    public void setHoldability(int holdability) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setHoldability(holdability));
    }


    public int getHoldability() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getHoldability()))).intValue();
    }


    public Savepoint setSavepoint() throws SQLException
    {
        return (Savepoint)this.jdbcInterceptor.get(() -> this.target.setSavepoint());
    }


    public Savepoint setSavepoint(String name) throws SQLException
    {
        return (Savepoint)this.jdbcInterceptor.get(() -> this.target.setSavepoint(name));
    }


    public void rollback(Savepoint savepoint) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.rollback(savepoint));
    }


    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.releaseSavepoint(savepoint));
    }


    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return (Statement)this.jdbcInterceptor.get(() -> this.target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }


    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }


    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        return (CallableStatement)this.jdbcInterceptor.get(() -> this.target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }


    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql, autoGeneratedKeys));
    }


    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql, columnIndexes));
    }


    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        return (PreparedStatement)this.jdbcInterceptor.get(() -> this.target.prepareStatement(sql, columnNames));
    }


    public Clob createClob() throws SQLException
    {
        return (Clob)this.jdbcInterceptor.get(() -> this.target.createClob());
    }


    public Blob createBlob() throws SQLException
    {
        return (Blob)this.jdbcInterceptor.get(() -> this.target.createBlob());
    }


    public NClob createNClob() throws SQLException
    {
        return (NClob)this.jdbcInterceptor.get(() -> this.target.createNClob());
    }


    public SQLXML createSQLXML() throws SQLException
    {
        return (SQLXML)this.jdbcInterceptor.get(() -> this.target.createSQLXML());
    }


    public boolean isValid(int timeout) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isValid(timeout)))).booleanValue();
    }


    public void setClientInfo(String name, String value) throws SQLClientInfoException
    {
        try
        {
            this.jdbcInterceptor.run(() -> this.target.setClientInfo(name, value));
        }
        catch(SQLClientInfoException e)
        {
            throw e;
        }
        catch(SQLException e)
        {
            throw new SQLClientInfoException(Collections.emptyMap(), e);
        }
    }


    public void setClientInfo(Properties properties) throws SQLClientInfoException
    {
        try
        {
            this.jdbcInterceptor.run(() -> this.target.setClientInfo(properties));
        }
        catch(SQLClientInfoException e)
        {
            throw e;
        }
        catch(SQLException e)
        {
            throw new SQLClientInfoException(Collections.emptyMap(), e);
        }
    }


    public String getClientInfo(String name) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getClientInfo(name));
    }


    public Properties getClientInfo() throws SQLException
    {
        return (Properties)this.jdbcInterceptor.get(() -> this.target.getClientInfo());
    }


    public Array createArrayOf(String typeName, Object[] elements) throws SQLException
    {
        return (Array)this.jdbcInterceptor.get(() -> this.target.createArrayOf(typeName, elements));
    }


    public Struct createStruct(String typeName, Object[] attributes) throws SQLException
    {
        return (Struct)this.jdbcInterceptor.get(() -> this.target.createStruct(typeName, attributes));
    }


    public void setSchema(String schema) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setSchema(schema));
    }


    public String getSchema() throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getSchema());
    }


    public void abort(Executor executor) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.abort(executor));
    }


    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNetworkTimeout(executor, milliseconds));
    }


    public int getNetworkTimeout() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getNetworkTimeout()))).intValue();
    }
}
