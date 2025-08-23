package de.hybris.platform.jdbcwrapper.interceptor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Objects;

class StatementWithJDBCInterceptor implements Statement
{
    private final Statement target;
    private final JDBCInterceptor jdbcInterceptor;


    public StatementWithJDBCInterceptor(Statement target, JDBCInterceptor jdbcInterceptor)
    {
        this.target = Objects.<Statement>requireNonNull(target, "target mustn't be null.");
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


    public ResultSet executeQuery(String sql) throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.executeQuery(sql));
    }


    public int executeUpdate(String sql) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql)))).intValue();
    }


    public void close() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.close());
    }


    public int getMaxFieldSize() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getMaxFieldSize()))).intValue();
    }


    public void setMaxFieldSize(int max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setMaxFieldSize(max));
    }


    public int getMaxRows() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getMaxRows()))).intValue();
    }


    public void setMaxRows(int max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setMaxRows(max));
    }


    public void setEscapeProcessing(boolean enable) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setEscapeProcessing(enable));
    }


    public int getQueryTimeout() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getQueryTimeout()))).intValue();
    }


    public void setQueryTimeout(int seconds) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setQueryTimeout(seconds));
    }


    public void cancel() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.cancel());
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return (SQLWarning)this.jdbcInterceptor.get(() -> this.target.getWarnings());
    }


    public void clearWarnings() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearWarnings());
    }


    public void setCursorName(String name) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCursorName(name));
    }


    public boolean execute(String sql) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql)))).booleanValue();
    }


    public ResultSet getResultSet() throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.getResultSet());
    }


    public int getUpdateCount() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getUpdateCount()))).intValue();
    }


    public boolean getMoreResults() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getMoreResults()))).booleanValue();
    }


    public void setFetchDirection(int direction) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setFetchDirection(direction));
    }


    public int getFetchDirection() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getFetchDirection()))).intValue();
    }


    public void setFetchSize(int rows) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setFetchSize(rows));
    }


    public int getFetchSize() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getFetchSize()))).intValue();
    }


    public int getResultSetConcurrency() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getResultSetConcurrency()))).intValue();
    }


    public int getResultSetType() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getResultSetType()))).intValue();
    }


    public void addBatch(String sql) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.addBatch(sql));
    }


    public void clearBatch() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearBatch());
    }


    public int[] executeBatch() throws SQLException
    {
        return (int[])this.jdbcInterceptor.get(() -> this.target.executeBatch());
    }


    public Connection getConnection() throws SQLException
    {
        return (Connection)this.jdbcInterceptor.get(() -> this.target.getConnection());
    }


    public boolean getMoreResults(int current) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getMoreResults(current)))).booleanValue();
    }


    public ResultSet getGeneratedKeys() throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.getGeneratedKeys());
    }


    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, autoGeneratedKeys)))).intValue();
    }


    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, columnIndexes)))).intValue();
    }


    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, columnNames)))).intValue();
    }


    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, autoGeneratedKeys)))).booleanValue();
    }


    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, columnIndexes)))).booleanValue();
    }


    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, columnNames)))).booleanValue();
    }


    public int getResultSetHoldability() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getResultSetHoldability()))).intValue();
    }


    public boolean isClosed() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isClosed()))).booleanValue();
    }


    public void setPoolable(boolean poolable) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setPoolable(poolable));
    }


    public boolean isPoolable() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isPoolable()))).booleanValue();
    }


    public void closeOnCompletion() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.closeOnCompletion());
    }


    public boolean isCloseOnCompletion() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isCloseOnCompletion()))).booleanValue();
    }


    public long getLargeUpdateCount() throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLargeUpdateCount()))).longValue();
    }


    public void setLargeMaxRows(long max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setLargeMaxRows(max));
    }


    public long getLargeMaxRows() throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLargeMaxRows()))).longValue();
    }


    public long[] executeLargeBatch() throws SQLException
    {
        return (long[])this.jdbcInterceptor.get(() -> this.target.executeLargeBatch());
    }


    public long executeLargeUpdate(String sql) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql)))).longValue();
    }


    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, autoGeneratedKeys)))).longValue();
    }


    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, columnIndexes)))).longValue();
    }


    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, columnNames)))).longValue();
    }
}
