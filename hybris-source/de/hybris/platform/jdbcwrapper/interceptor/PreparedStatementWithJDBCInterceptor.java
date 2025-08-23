package de.hybris.platform.jdbcwrapper.interceptor;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

class PreparedStatementWithJDBCInterceptor implements PreparedStatement
{
    private final PreparedStatement target;
    private final JDBCInterceptor jdbcInterceptor;


    public PreparedStatementWithJDBCInterceptor(PreparedStatement target, JDBCInterceptor jdbcInterceptor)
    {
        this.target = Objects.<PreparedStatement>requireNonNull(target, "target mustn't be null.");
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


    public ResultSet executeQuery() throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.executeQuery());
    }


    public int executeUpdate(String sql) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql)))).intValue();
    }


    public int executeUpdate() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate()))).intValue();
    }


    public void setNull(int parameterIndex, int sqlType) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNull(parameterIndex, sqlType));
    }


    public void close() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.close());
    }


    public int getMaxFieldSize() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getMaxFieldSize()))).intValue();
    }


    public void setBoolean(int parameterIndex, boolean x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBoolean(parameterIndex, x));
    }


    public void setByte(int parameterIndex, byte x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setByte(parameterIndex, x));
    }


    public void setMaxFieldSize(int max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setMaxFieldSize(max));
    }


    public void setShort(int parameterIndex, short x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setShort(parameterIndex, x));
    }


    public int getMaxRows() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getMaxRows()))).intValue();
    }


    public void setInt(int parameterIndex, int x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setInt(parameterIndex, x));
    }


    public void setMaxRows(int max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setMaxRows(max));
    }


    public void setLong(int parameterIndex, long x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setLong(parameterIndex, x));
    }


    public void setFloat(int parameterIndex, float x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setFloat(parameterIndex, x));
    }


    public void setEscapeProcessing(boolean enable) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setEscapeProcessing(enable));
    }


    public void setDouble(int parameterIndex, double x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setDouble(parameterIndex, x));
    }


    public int getQueryTimeout() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getQueryTimeout()))).intValue();
    }


    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBigDecimal(parameterIndex, x));
    }


    public void setQueryTimeout(int seconds) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setQueryTimeout(seconds));
    }


    public void setString(int parameterIndex, String x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setString(parameterIndex, x));
    }


    public void setBytes(int parameterIndex, byte[] x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBytes(parameterIndex, x));
    }


    public void cancel() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.cancel());
    }


    public void setDate(int parameterIndex, Date x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setDate(parameterIndex, x));
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return (SQLWarning)this.jdbcInterceptor.get(() -> this.target.getWarnings());
    }


    public void setTime(int parameterIndex, Time x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTime(parameterIndex, x));
    }


    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTimestamp(parameterIndex, x));
    }


    public void clearWarnings() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearWarnings());
    }


    public void setCursorName(String name) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCursorName(name));
    }


    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setAsciiStream(parameterIndex, x, length));
    }


    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setUnicodeStream(parameterIndex, x, length));
    }


    public boolean execute(String sql) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql)))).booleanValue();
    }


    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBinaryStream(parameterIndex, x, length));
    }


    public ResultSet getResultSet() throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.getResultSet());
    }


    public int getUpdateCount() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getUpdateCount()))).intValue();
    }


    public void clearParameters() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearParameters());
    }


    public boolean getMoreResults() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getMoreResults()))).booleanValue();
    }


    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setObject(parameterIndex, x, targetSqlType));
    }


    public void setFetchDirection(int direction) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setFetchDirection(direction));
    }


    public void setObject(int parameterIndex, Object x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setObject(parameterIndex, x));
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


    public boolean execute() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute()))).booleanValue();
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


    public void addBatch() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.addBatch());
    }


    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCharacterStream(parameterIndex, reader, length));
    }


    public void clearBatch() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearBatch());
    }


    public int[] executeBatch() throws SQLException
    {
        return (int[])this.jdbcInterceptor.get(() -> this.target.executeBatch());
    }


    public void setRef(int parameterIndex, Ref x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setRef(parameterIndex, x));
    }


    public void setBlob(int parameterIndex, Blob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBlob(parameterIndex, x));
    }


    public void setClob(int parameterIndex, Clob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setClob(parameterIndex, x));
    }


    public void setArray(int parameterIndex, Array x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setArray(parameterIndex, x));
    }


    public Connection getConnection() throws SQLException
    {
        return (Connection)this.jdbcInterceptor.get(() -> this.target.getConnection());
    }


    public ResultSetMetaData getMetaData() throws SQLException
    {
        return (ResultSetMetaData)this.jdbcInterceptor.get(() -> this.target.getMetaData());
    }


    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setDate(parameterIndex, x, cal));
    }


    public boolean getMoreResults(int current) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getMoreResults(current)))).booleanValue();
    }


    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTime(parameterIndex, x, cal));
    }


    public ResultSet getGeneratedKeys() throws SQLException
    {
        return (ResultSet)this.jdbcInterceptor.get(() -> this.target.getGeneratedKeys());
    }


    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setTimestamp(parameterIndex, x, cal));
    }


    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, autoGeneratedKeys)))).intValue();
    }


    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNull(parameterIndex, sqlType, typeName));
    }


    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, columnIndexes)))).intValue();
    }


    public void setURL(int parameterIndex, URL x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setURL(parameterIndex, x));
    }


    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        return (ParameterMetaData)this.jdbcInterceptor.get(() -> this.target.getParameterMetaData());
    }


    public void setRowId(int parameterIndex, RowId x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setRowId(parameterIndex, x));
    }


    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.executeUpdate(sql, columnNames)))).intValue();
    }


    public void setNString(int parameterIndex, String value) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNString(parameterIndex, value));
    }


    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNCharacterStream(parameterIndex, value, length));
    }


    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, autoGeneratedKeys)))).booleanValue();
    }


    public void setNClob(int parameterIndex, NClob value) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNClob(parameterIndex, value));
    }


    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setClob(parameterIndex, reader, length));
    }


    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, columnIndexes)))).booleanValue();
    }


    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBlob(parameterIndex, inputStream, length));
    }


    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNClob(parameterIndex, reader, length));
    }


    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.execute(sql, columnNames)))).booleanValue();
    }


    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setSQLXML(parameterIndex, xmlObject));
    }


    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setObject(parameterIndex, x, targetSqlType, scaleOrLength));
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


    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setAsciiStream(parameterIndex, x, length));
    }


    public boolean isPoolable() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isPoolable()))).booleanValue();
    }


    public void closeOnCompletion() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.closeOnCompletion());
    }


    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBinaryStream(parameterIndex, x, length));
    }


    public boolean isCloseOnCompletion() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isCloseOnCompletion()))).booleanValue();
    }


    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCharacterStream(parameterIndex, reader, length));
    }


    public long getLargeUpdateCount() throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLargeUpdateCount()))).longValue();
    }


    public void setLargeMaxRows(long max) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setLargeMaxRows(max));
    }


    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setAsciiStream(parameterIndex, x));
    }


    public long getLargeMaxRows() throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLargeMaxRows()))).longValue();
    }


    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBinaryStream(parameterIndex, x));
    }


    public long[] executeLargeBatch() throws SQLException
    {
        return (long[])this.jdbcInterceptor.get(() -> this.target.executeLargeBatch());
    }


    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setCharacterStream(parameterIndex, reader));
    }


    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNCharacterStream(parameterIndex, value));
    }


    public long executeLargeUpdate(String sql) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql)))).longValue();
    }


    public void setClob(int parameterIndex, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setClob(parameterIndex, reader));
    }


    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, autoGeneratedKeys)))).longValue();
    }


    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setBlob(parameterIndex, inputStream));
    }


    public void setNClob(int parameterIndex, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setNClob(parameterIndex, reader));
    }


    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, columnIndexes)))).longValue();
    }


    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setObject(parameterIndex, x, targetSqlType, scaleOrLength));
    }


    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate(sql, columnNames)))).longValue();
    }


    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.setObject(parameterIndex, x, targetSqlType));
    }


    public long executeLargeUpdate() throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.executeLargeUpdate()))).longValue();
    }
}
