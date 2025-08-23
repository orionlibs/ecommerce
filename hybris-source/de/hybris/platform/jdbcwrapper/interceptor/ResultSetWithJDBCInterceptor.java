package de.hybris.platform.jdbcwrapper.interceptor;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

class ResultSetWithJDBCInterceptor implements ResultSet
{
    private final ResultSet target;
    private final JDBCInterceptor jdbcInterceptor;


    public ResultSetWithJDBCInterceptor(ResultSet target, JDBCInterceptor jdbcInterceptor)
    {
        this.target = Objects.<ResultSet>requireNonNull(target, "target mustn't be null.");
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


    public boolean next() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.next()))).booleanValue();
    }


    public void close() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.close());
    }


    public boolean wasNull() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.wasNull()))).booleanValue();
    }


    public String getString(int columnIndex) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getString(columnIndex));
    }


    public boolean getBoolean(int columnIndex) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getBoolean(columnIndex)))).booleanValue();
    }


    public byte getByte(int columnIndex) throws SQLException
    {
        return ((Byte)this.jdbcInterceptor.get(() -> Byte.valueOf(this.target.getByte(columnIndex)))).byteValue();
    }


    public short getShort(int columnIndex) throws SQLException
    {
        return ((Short)this.jdbcInterceptor.get(() -> Short.valueOf(this.target.getShort(columnIndex)))).shortValue();
    }


    public int getInt(int columnIndex) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getInt(columnIndex)))).intValue();
    }


    public long getLong(int columnIndex) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLong(columnIndex)))).longValue();
    }


    public float getFloat(int columnIndex) throws SQLException
    {
        return ((Float)this.jdbcInterceptor.get(() -> Float.valueOf(this.target.getFloat(columnIndex)))).floatValue();
    }


    public double getDouble(int columnIndex) throws SQLException
    {
        return ((Double)this.jdbcInterceptor.get(() -> Double.valueOf(this.target.getDouble(columnIndex)))).doubleValue();
    }


    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
    {
        return (BigDecimal)this.jdbcInterceptor.get(() -> this.target.getBigDecimal(columnIndex, scale));
    }


    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return (byte[])this.jdbcInterceptor.get(() -> this.target.getBytes(columnIndex));
    }


    public Date getDate(int columnIndex) throws SQLException
    {
        return (Date)this.jdbcInterceptor.get(() -> this.target.getDate(columnIndex));
    }


    public Time getTime(int columnIndex) throws SQLException
    {
        return (Time)this.jdbcInterceptor.get(() -> this.target.getTime(columnIndex));
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return (Timestamp)this.jdbcInterceptor.get(() -> this.target.getTimestamp(columnIndex));
    }


    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getAsciiStream(columnIndex));
    }


    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getUnicodeStream(columnIndex));
    }


    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getBinaryStream(columnIndex));
    }


    public String getString(String columnLabel) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getString(columnLabel));
    }


    public boolean getBoolean(String columnLabel) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.getBoolean(columnLabel)))).booleanValue();
    }


    public byte getByte(String columnLabel) throws SQLException
    {
        return ((Byte)this.jdbcInterceptor.get(() -> Byte.valueOf(this.target.getByte(columnLabel)))).byteValue();
    }


    public short getShort(String columnLabel) throws SQLException
    {
        return ((Short)this.jdbcInterceptor.get(() -> Short.valueOf(this.target.getShort(columnLabel)))).shortValue();
    }


    public int getInt(String columnLabel) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getInt(columnLabel)))).intValue();
    }


    public long getLong(String columnLabel) throws SQLException
    {
        return ((Long)this.jdbcInterceptor.get(() -> Long.valueOf(this.target.getLong(columnLabel)))).longValue();
    }


    public float getFloat(String columnLabel) throws SQLException
    {
        return ((Float)this.jdbcInterceptor.get(() -> Float.valueOf(this.target.getFloat(columnLabel)))).floatValue();
    }


    public double getDouble(String columnLabel) throws SQLException
    {
        return ((Double)this.jdbcInterceptor.get(() -> Double.valueOf(this.target.getDouble(columnLabel)))).doubleValue();
    }


    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException
    {
        return (BigDecimal)this.jdbcInterceptor.get(() -> this.target.getBigDecimal(columnLabel, scale));
    }


    public byte[] getBytes(String columnLabel) throws SQLException
    {
        return (byte[])this.jdbcInterceptor.get(() -> this.target.getBytes(columnLabel));
    }


    public Date getDate(String columnLabel) throws SQLException
    {
        return (Date)this.jdbcInterceptor.get(() -> this.target.getDate(columnLabel));
    }


    public Time getTime(String columnLabel) throws SQLException
    {
        return (Time)this.jdbcInterceptor.get(() -> this.target.getTime(columnLabel));
    }


    public Timestamp getTimestamp(String columnLabel) throws SQLException
    {
        return (Timestamp)this.jdbcInterceptor.get(() -> this.target.getTimestamp(columnLabel));
    }


    public InputStream getAsciiStream(String columnLabel) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getAsciiStream(columnLabel));
    }


    public InputStream getUnicodeStream(String columnLabel) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getUnicodeStream(columnLabel));
    }


    public InputStream getBinaryStream(String columnLabel) throws SQLException
    {
        return (InputStream)this.jdbcInterceptor.get(() -> this.target.getBinaryStream(columnLabel));
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return (SQLWarning)this.jdbcInterceptor.get(() -> this.target.getWarnings());
    }


    public void clearWarnings() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.clearWarnings());
    }


    public String getCursorName() throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getCursorName());
    }


    public ResultSetMetaData getMetaData() throws SQLException
    {
        return (ResultSetMetaData)this.jdbcInterceptor.get(() -> this.target.getMetaData());
    }


    public Object getObject(int columnIndex) throws SQLException
    {
        return this.jdbcInterceptor.get(() -> this.target.getObject(columnIndex));
    }


    public Object getObject(String columnLabel) throws SQLException
    {
        return this.jdbcInterceptor.get(() -> this.target.getObject(columnLabel));
    }


    public int findColumn(String columnLabel) throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.findColumn(columnLabel)))).intValue();
    }


    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return (Reader)this.jdbcInterceptor.get(() -> this.target.getCharacterStream(columnIndex));
    }


    public Reader getCharacterStream(String columnLabel) throws SQLException
    {
        return (Reader)this.jdbcInterceptor.get(() -> this.target.getCharacterStream(columnLabel));
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return (BigDecimal)this.jdbcInterceptor.get(() -> this.target.getBigDecimal(columnIndex));
    }


    public BigDecimal getBigDecimal(String columnLabel) throws SQLException
    {
        return (BigDecimal)this.jdbcInterceptor.get(() -> this.target.getBigDecimal(columnLabel));
    }


    public boolean isBeforeFirst() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isBeforeFirst()))).booleanValue();
    }


    public boolean isAfterLast() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isAfterLast()))).booleanValue();
    }


    public boolean isFirst() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isFirst()))).booleanValue();
    }


    public boolean isLast() throws SQLException
    {
        Objects.requireNonNull(this.target);
        return ((Boolean)this.jdbcInterceptor.get(this.target::isLast)).booleanValue();
    }


    public void beforeFirst() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.beforeFirst());
    }


    public void afterLast() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.afterLast());
    }


    public boolean first() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.first()))).booleanValue();
    }


    public boolean last() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.last()))).booleanValue();
    }


    public int getRow() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getRow()))).intValue();
    }


    public boolean absolute(int row) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.absolute(row)))).booleanValue();
    }


    public boolean relative(int rows) throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.relative(rows)))).booleanValue();
    }


    public boolean previous() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.previous()))).booleanValue();
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


    public int getType() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getType()))).intValue();
    }


    public int getConcurrency() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getConcurrency()))).intValue();
    }


    public boolean rowUpdated() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.rowUpdated()))).booleanValue();
    }


    public boolean rowInserted() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.rowInserted()))).booleanValue();
    }


    public boolean rowDeleted() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.rowDeleted()))).booleanValue();
    }


    public void updateNull(int columnIndex) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNull(columnIndex));
    }


    public void updateBoolean(int columnIndex, boolean x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBoolean(columnIndex, x));
    }


    public void updateByte(int columnIndex, byte x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateByte(columnIndex, x));
    }


    public void updateShort(int columnIndex, short x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateShort(columnIndex, x));
    }


    public void updateInt(int columnIndex, int x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateInt(columnIndex, x));
    }


    public void updateLong(int columnIndex, long x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateLong(columnIndex, x));
    }


    public void updateFloat(int columnIndex, float x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateFloat(columnIndex, x));
    }


    public void updateDouble(int columnIndex, double x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateDouble(columnIndex, x));
    }


    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBigDecimal(columnIndex, x));
    }


    public void updateString(int columnIndex, String x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateString(columnIndex, x));
    }


    public void updateBytes(int columnIndex, byte[] x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBytes(columnIndex, x));
    }


    public void updateDate(int columnIndex, Date x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateDate(columnIndex, x));
    }


    public void updateTime(int columnIndex, Time x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateTime(columnIndex, x));
    }


    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateTimestamp(columnIndex, x));
    }


    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnIndex, x, length));
    }


    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnIndex, x, length));
    }


    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnIndex, x, length));
    }


    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnIndex, x, scaleOrLength));
    }


    public void updateObject(int columnIndex, Object x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnIndex, x));
    }


    public void updateNull(String columnLabel) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNull(columnLabel));
    }


    public void updateBoolean(String columnLabel, boolean x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBoolean(columnLabel, x));
    }


    public void updateByte(String columnLabel, byte x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateByte(columnLabel, x));
    }


    public void updateShort(String columnLabel, short x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateShort(columnLabel, x));
    }


    public void updateInt(String columnLabel, int x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateInt(columnLabel, x));
    }


    public void updateLong(String columnLabel, long x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateLong(columnLabel, x));
    }


    public void updateFloat(String columnLabel, float x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateFloat(columnLabel, x));
    }


    public void updateDouble(String columnLabel, double x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateDouble(columnLabel, x));
    }


    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBigDecimal(columnLabel, x));
    }


    public void updateString(String columnLabel, String x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateString(columnLabel, x));
    }


    public void updateBytes(String columnLabel, byte[] x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBytes(columnLabel, x));
    }


    public void updateDate(String columnLabel, Date x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateDate(columnLabel, x));
    }


    public void updateTime(String columnLabel, Time x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateTime(columnLabel, x));
    }


    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateTimestamp(columnLabel, x));
    }


    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnLabel, x, length));
    }


    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnLabel, x, length));
    }


    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnLabel, reader, length));
    }


    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnLabel, x, scaleOrLength));
    }


    public void updateObject(String columnLabel, Object x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnLabel, x));
    }


    public void insertRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.insertRow());
    }


    public void updateRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateRow());
    }


    public void deleteRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.deleteRow());
    }


    public void refreshRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.refreshRow());
    }


    public void cancelRowUpdates() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.cancelRowUpdates());
    }


    public void moveToInsertRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.moveToInsertRow());
    }


    public void moveToCurrentRow() throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.moveToCurrentRow());
    }


    public Statement getStatement() throws SQLException
    {
        return (Statement)this.jdbcInterceptor.get(() -> this.target.getStatement());
    }


    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException
    {
        return this.jdbcInterceptor.get(() -> this.target.getObject(columnIndex, map));
    }


    public Ref getRef(int columnIndex) throws SQLException
    {
        return (Ref)this.jdbcInterceptor.get(() -> this.target.getRef(columnIndex));
    }


    public Blob getBlob(int columnIndex) throws SQLException
    {
        return (Blob)this.jdbcInterceptor.get(() -> this.target.getBlob(columnIndex));
    }


    public Clob getClob(int columnIndex) throws SQLException
    {
        return (Clob)this.jdbcInterceptor.get(() -> this.target.getClob(columnIndex));
    }


    public Array getArray(int columnIndex) throws SQLException
    {
        return (Array)this.jdbcInterceptor.get(() -> this.target.getArray(columnIndex));
    }


    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException
    {
        return this.jdbcInterceptor.get(() -> this.target.getObject(columnLabel, map));
    }


    public Ref getRef(String columnLabel) throws SQLException
    {
        return (Ref)this.jdbcInterceptor.get(() -> this.target.getRef(columnLabel));
    }


    public Blob getBlob(String columnLabel) throws SQLException
    {
        return (Blob)this.jdbcInterceptor.get(() -> this.target.getBlob(columnLabel));
    }


    public Clob getClob(String columnLabel) throws SQLException
    {
        return (Clob)this.jdbcInterceptor.get(() -> this.target.getClob(columnLabel));
    }


    public Array getArray(String columnLabel) throws SQLException
    {
        return (Array)this.jdbcInterceptor.get(() -> this.target.getArray(columnLabel));
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return (Date)this.jdbcInterceptor.get(() -> this.target.getDate(columnIndex, cal));
    }


    public Date getDate(String columnLabel, Calendar cal) throws SQLException
    {
        return (Date)this.jdbcInterceptor.get(() -> this.target.getDate(columnLabel, cal));
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return (Time)this.jdbcInterceptor.get(() -> this.target.getTime(columnIndex, cal));
    }


    public Time getTime(String columnLabel, Calendar cal) throws SQLException
    {
        return (Time)this.jdbcInterceptor.get(() -> this.target.getTime(columnLabel, cal));
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
    {
        return (Timestamp)this.jdbcInterceptor.get(() -> this.target.getTimestamp(columnIndex, cal));
    }


    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException
    {
        return (Timestamp)this.jdbcInterceptor.get(() -> this.target.getTimestamp(columnLabel, cal));
    }


    public URL getURL(int columnIndex) throws SQLException
    {
        return (URL)this.jdbcInterceptor.get(() -> this.target.getURL(columnIndex));
    }


    public URL getURL(String columnLabel) throws SQLException
    {
        return (URL)this.jdbcInterceptor.get(() -> this.target.getURL(columnLabel));
    }


    public void updateRef(int columnIndex, Ref x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateRef(columnIndex, x));
    }


    public void updateRef(String columnLabel, Ref x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateRef(columnLabel, x));
    }


    public void updateBlob(int columnIndex, Blob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnIndex, x));
    }


    public void updateBlob(String columnLabel, Blob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnLabel, x));
    }


    public void updateClob(int columnIndex, Clob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnIndex, x));
    }


    public void updateClob(String columnLabel, Clob x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnLabel, x));
    }


    public void updateArray(int columnIndex, Array x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateArray(columnIndex, x));
    }


    public void updateArray(String columnLabel, Array x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateArray(columnLabel, x));
    }


    public RowId getRowId(int columnIndex) throws SQLException
    {
        return (RowId)this.jdbcInterceptor.get(() -> this.target.getRowId(columnIndex));
    }


    public RowId getRowId(String columnLabel) throws SQLException
    {
        return (RowId)this.jdbcInterceptor.get(() -> this.target.getRowId(columnLabel));
    }


    public void updateRowId(int columnIndex, RowId x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateRowId(columnIndex, x));
    }


    public void updateRowId(String columnLabel, RowId x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateRowId(columnLabel, x));
    }


    public int getHoldability() throws SQLException
    {
        return ((Integer)this.jdbcInterceptor.get(() -> Integer.valueOf(this.target.getHoldability()))).intValue();
    }


    public boolean isClosed() throws SQLException
    {
        return ((Boolean)this.jdbcInterceptor.get(() -> Boolean.valueOf(this.target.isClosed()))).booleanValue();
    }


    public void updateNString(int columnIndex, String nString) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNString(columnIndex, nString));
    }


    public void updateNString(String columnLabel, String nString) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNString(columnLabel, nString));
    }


    public void updateNClob(int columnIndex, NClob nClob) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnIndex, nClob));
    }


    public void updateNClob(String columnLabel, NClob nClob) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnLabel, nClob));
    }


    public NClob getNClob(int columnIndex) throws SQLException
    {
        return (NClob)this.jdbcInterceptor.get(() -> this.target.getNClob(columnIndex));
    }


    public NClob getNClob(String columnLabel) throws SQLException
    {
        return (NClob)this.jdbcInterceptor.get(() -> this.target.getNClob(columnLabel));
    }


    public SQLXML getSQLXML(int columnIndex) throws SQLException
    {
        return (SQLXML)this.jdbcInterceptor.get(() -> this.target.getSQLXML(columnIndex));
    }


    public SQLXML getSQLXML(String columnLabel) throws SQLException
    {
        return (SQLXML)this.jdbcInterceptor.get(() -> this.target.getSQLXML(columnLabel));
    }


    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateSQLXML(columnIndex, xmlObject));
    }


    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateSQLXML(columnLabel, xmlObject));
    }


    public String getNString(int columnIndex) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getNString(columnIndex));
    }


    public String getNString(String columnLabel) throws SQLException
    {
        return (String)this.jdbcInterceptor.get(() -> this.target.getNString(columnLabel));
    }


    public Reader getNCharacterStream(int columnIndex) throws SQLException
    {
        return (Reader)this.jdbcInterceptor.get(() -> this.target.getNCharacterStream(columnIndex));
    }


    public Reader getNCharacterStream(String columnLabel) throws SQLException
    {
        return (Reader)this.jdbcInterceptor.get(() -> this.target.getNCharacterStream(columnLabel));
    }


    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNCharacterStream(columnIndex, x, length));
    }


    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNCharacterStream(columnLabel, reader, length));
    }


    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnIndex, x, length));
    }


    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnIndex, x, length));
    }


    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnIndex, x, length));
    }


    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnLabel, x, length));
    }


    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnLabel, x, length));
    }


    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnLabel, reader, length));
    }


    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnIndex, inputStream, length));
    }


    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnLabel, inputStream, length));
    }


    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnIndex, reader, length));
    }


    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnLabel, reader, length));
    }


    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnIndex, reader, length));
    }


    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnLabel, reader, length));
    }


    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNCharacterStream(columnIndex, x));
    }


    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNCharacterStream(columnLabel, reader));
    }


    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnIndex, x));
    }


    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnIndex, x));
    }


    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnIndex, x));
    }


    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateAsciiStream(columnLabel, x));
    }


    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBinaryStream(columnLabel, x));
    }


    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateCharacterStream(columnLabel, reader));
    }


    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnIndex, inputStream));
    }


    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateBlob(columnLabel, inputStream));
    }


    public void updateClob(int columnIndex, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnIndex, reader));
    }


    public void updateClob(String columnLabel, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateClob(columnLabel, reader));
    }


    public void updateNClob(int columnIndex, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnIndex, reader));
    }


    public void updateNClob(String columnLabel, Reader reader) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateNClob(columnLabel, reader));
    }


    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException
    {
        return (T)this.jdbcInterceptor.get(() -> this.target.getObject(columnIndex, type));
    }


    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException
    {
        return (T)this.jdbcInterceptor.get(() -> this.target.getObject(columnLabel, type));
    }


    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnIndex, x, targetSqlType, scaleOrLength));
    }


    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnLabel, x, targetSqlType, scaleOrLength));
    }


    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnIndex, x, targetSqlType));
    }


    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException
    {
        this.jdbcInterceptor.run(() -> this.target.updateObject(columnLabel, x, targetSqlType));
    }
}
