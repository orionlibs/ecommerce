package de.hybris.platform.jdbcwrapper;

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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class ResultSetImpl implements ResultSet
{
    private final boolean doLog;
    private final ResultSet passthru;
    private final StatementImpl statement;
    private volatile boolean closed = false;
    private Map<String, String> resultMap;
    private int currRow = -1;


    public ResultSetImpl(StatementImpl statement, ResultSet resultSet)
    {
        this.passthru = resultSet;
        this.statement = statement;
        this.doLog = this.statement.connection.getDataSource().getLogUtils().isLoggingActivated();
    }


    private boolean isLoggingActivated()
    {
        return this.doLog;
    }


    public Statement getStatement() throws SQLException
    {
        return (Statement)this.statement;
    }


    public boolean next() throws SQLException
    {
        try
        {
            return nextInternal();
        }
        catch(SQLException e)
        {
            ((ConnectionImpl)getStatement().getConnection()).notifyError(e);
            ((ConnectionImpl)getStatement().getConnection()).logError(e, "error fetching next result");
            throw e;
        }
    }


    private void logValue(String col, String value)
    {
        if(this.resultMap == null)
        {
            this.resultMap = new TreeMap<>();
        }
        this.resultMap.put(col, value);
    }


    private boolean nextInternal() throws SQLException
    {
        if(isLoggingActivated())
        {
            logNext();
        }
        return this.passthru.next();
    }


    private final void logNext() throws SQLException
    {
        if(this.currRow > -1)
        {
            int id = this.statement.getConnection().getConnectionID();
            if(this.resultMap != null)
            {
                StringBuilder buffer = new StringBuilder();
                String comma = "";
                for(Map.Entry<String, String> e : this.resultMap.entrySet())
                {
                    buffer.append(comma);
                    buffer.append(e.getKey());
                    buffer.append(" = ");
                    buffer.append(e.getValue());
                    comma = ", ";
                }
                this.statement.connection
                                .getDataSource()
                                .getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), id, 0L, "resultset", "next()", " previous results read: " + buffer
                                                .toString());
            }
            else
            {
                this.statement.connection.getDataSource().getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), id, 0L, "resultset", "next()", " previous results read: n/a");
            }
        }
        this.currRow++;
        if(this.resultMap != null)
        {
            this.resultMap.clear();
        }
    }


    public String getString(String columnLable) throws SQLException
    {
        String result = this.passthru.getString(columnLable);
        if(isLoggingActivated())
        {
            logValue(columnLable, result);
        }
        return result;
    }


    public String getString(int columnIndex) throws SQLException
    {
        String result = this.passthru.getString(columnIndex);
        if(isLoggingActivated())
        {
            logValue(this.passthru.getMetaData().getColumnName(columnIndex), result);
        }
        return result;
    }


    public short getShort(String columnLable) throws SQLException
    {
        short result = this.passthru.getShort(columnLable);
        if(isLoggingActivated())
        {
            logValue(columnLable, String.valueOf(result));
        }
        return result;
    }


    public short getShort(int columnIndex) throws SQLException
    {
        short result = this.passthru.getShort(columnIndex);
        if(isLoggingActivated())
        {
            logValue(this.passthru.getMetaData().getColumnName(columnIndex), String.valueOf(result));
        }
        return result;
    }


    public int getInt(int columnIndex) throws SQLException
    {
        int result = this.passthru.getInt(columnIndex);
        if(isLoggingActivated())
        {
            logValue(this.passthru.getMetaData().getColumnName(columnIndex), String.valueOf(result));
        }
        return result;
    }


    public int getInt(String columnLable) throws SQLException
    {
        int result = this.passthru.getInt(columnLable);
        logValue(columnLable, String.valueOf(result));
        return result;
    }


    public int getRow() throws SQLException
    {
        return this.passthru.getRow();
    }


    public byte[] getBytes(String columnLable) throws SQLException
    {
        return this.passthru.getBytes(columnLable);
    }


    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return this.passthru.getBytes(columnIndex);
    }


    public boolean getBoolean(int columnIndex) throws SQLException
    {
        return this.passthru.getBoolean(columnIndex);
    }


    public boolean getBoolean(String columnLable) throws SQLException
    {
        return this.passthru.getBoolean(columnLable);
    }


    public int getType() throws SQLException
    {
        return this.passthru.getType();
    }


    public long getLong(int columnIndex) throws SQLException
    {
        return this.passthru.getLong(columnIndex);
    }


    public long getLong(String columnLable) throws SQLException
    {
        return this.passthru.getLong(columnLable);
    }


    public boolean previous() throws SQLException
    {
        return this.passthru.previous();
    }


    public void close() throws SQLException
    {
        try
        {
            this.passthru.close();
        }
        finally
        {
            this.closed = true;
            this.statement.notifyResultSetClosed(this);
        }
    }


    public Object getObject(int columnIndex) throws SQLException
    {
        return this.passthru.getObject(columnIndex);
    }


    public Object getObject(String columnLable) throws SQLException
    {
        return this.passthru.getObject(columnLable);
    }


    public Ref getRef(String columnLable) throws SQLException
    {
        return this.passthru.getRef(columnLable);
    }


    public Ref getRef(int columnIndex) throws SQLException
    {
        return this.passthru.getRef(columnIndex);
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return this.passthru.getTime(columnIndex, cal);
    }


    public Time getTime(String columnLable, Calendar cal) throws SQLException
    {
        return this.passthru.getTime(columnLable, cal);
    }


    public Time getTime(String columnLable) throws SQLException
    {
        return this.passthru.getTime(columnLable);
    }


    public Time getTime(int columnIndex) throws SQLException
    {
        return this.passthru.getTime(columnIndex);
    }


    public Date getDate(int columnIndex) throws SQLException
    {
        return this.passthru.getDate(columnIndex);
    }


    public Date getDate(String columnLable, Calendar cal) throws SQLException
    {
        return this.passthru.getDate(columnLable, cal);
    }


    public Date getDate(String columnLable) throws SQLException
    {
        return this.passthru.getDate(columnLable);
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return this.passthru.getDate(columnIndex, cal);
    }


    public boolean wasNull() throws SQLException
    {
        return this.passthru.wasNull();
    }


    public byte getByte(String columnLable) throws SQLException
    {
        return this.passthru.getByte(columnLable);
    }


    public byte getByte(int columnIndex) throws SQLException
    {
        return this.passthru.getByte(columnIndex);
    }


    public float getFloat(String columnLable) throws SQLException
    {
        return this.passthru.getFloat(columnLable);
    }


    public float getFloat(int columnIndex) throws SQLException
    {
        return this.passthru.getFloat(columnIndex);
    }


    public double getDouble(int columnIndex) throws SQLException
    {
        return this.passthru.getDouble(columnIndex);
    }


    public double getDouble(String columnLable) throws SQLException
    {
        return this.passthru.getDouble(columnLable);
    }


    public BigDecimal getBigDecimal(String columnLable) throws SQLException
    {
        return this.passthru.getBigDecimal(columnLable);
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return this.passthru.getBigDecimal(columnIndex);
    }


    public Timestamp getTimestamp(String columnLable) throws SQLException
    {
        return this.passthru.getTimestamp(columnLable);
    }


    public Timestamp getTimestamp(String columnLable, Calendar cal) throws SQLException
    {
        return this.passthru.getTimestamp(columnLable, cal);
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return this.passthru.getTimestamp(columnIndex);
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
    {
        return this.passthru.getTimestamp(columnIndex, cal);
    }


    public InputStream getAsciiStream(String columnLable) throws SQLException
    {
        return this.passthru.getAsciiStream(columnLable);
    }


    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return this.passthru.getAsciiStream(columnIndex);
    }


    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        return this.passthru.getBinaryStream(columnIndex);
    }


    public InputStream getBinaryStream(String columnLable) throws SQLException
    {
        return this.passthru.getBinaryStream(columnLable);
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return this.passthru.getWarnings();
    }


    public void clearWarnings() throws SQLException
    {
        this.passthru.clearWarnings();
    }


    public String getCursorName() throws SQLException
    {
        return this.passthru.getCursorName();
    }


    public ResultSetMetaData getMetaData() throws SQLException
    {
        return this.passthru.getMetaData();
    }


    public int findColumn(String columnLable) throws SQLException
    {
        return this.passthru.findColumn(columnLable);
    }


    public Reader getCharacterStream(String columnLable) throws SQLException
    {
        return this.passthru.getCharacterStream(columnLable);
    }


    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return this.passthru.getCharacterStream(columnIndex);
    }


    public boolean isBeforeFirst() throws SQLException
    {
        return this.passthru.isBeforeFirst();
    }


    public boolean isAfterLast() throws SQLException
    {
        return this.passthru.isAfterLast();
    }


    public boolean isFirst() throws SQLException
    {
        return this.passthru.isFirst();
    }


    public boolean isLast() throws SQLException
    {
        return this.passthru.isLast();
    }


    public void beforeFirst() throws SQLException
    {
        this.passthru.beforeFirst();
    }


    public void afterLast() throws SQLException
    {
        this.passthru.afterLast();
    }


    public boolean first() throws SQLException
    {
        return this.passthru.first();
    }


    public boolean last() throws SQLException
    {
        return this.passthru.last();
    }


    public boolean absolute(int columnIndex) throws SQLException
    {
        return this.passthru.absolute(columnIndex);
    }


    public boolean relative(int columnIndex) throws SQLException
    {
        return this.passthru.relative(columnIndex);
    }


    public void setFetchDirection(int columnIndex) throws SQLException
    {
        this.passthru.setFetchDirection(columnIndex);
    }


    public int getFetchDirection() throws SQLException
    {
        return this.passthru.getFetchDirection();
    }


    public void setFetchSize(int columnIndex) throws SQLException
    {
        this.passthru.setFetchSize(columnIndex);
    }


    public int getFetchSize() throws SQLException
    {
        return this.passthru.getFetchSize();
    }


    public int getConcurrency() throws SQLException
    {
        return this.passthru.getConcurrency();
    }


    public boolean rowUpdated() throws SQLException
    {
        return this.passthru.rowUpdated();
    }


    public boolean rowInserted() throws SQLException
    {
        return this.passthru.rowInserted();
    }


    public boolean rowDeleted() throws SQLException
    {
        return this.passthru.rowDeleted();
    }


    public void updateNull(int columnIndex) throws SQLException
    {
        this.passthru.updateNull(columnIndex);
    }


    public void updateNull(String columnLable) throws SQLException
    {
        this.passthru.updateNull(columnLable);
    }


    public void updateBoolean(int columnIndex, boolean booleanValue) throws SQLException
    {
        this.passthru.updateBoolean(columnIndex, booleanValue);
    }


    public void updateBoolean(String columnLable, boolean booleanValue) throws SQLException
    {
        this.passthru.updateBoolean(columnLable, booleanValue);
    }


    public void updateByte(String columnLable, byte byteValue) throws SQLException
    {
        this.passthru.updateByte(columnLable, byteValue);
    }


    public void updateByte(int columnIndex, byte byteValue) throws SQLException
    {
        this.passthru.updateByte(columnIndex, byteValue);
    }


    public void updateShort(int columnIndex, short shortValue) throws SQLException
    {
        this.passthru.updateShort(columnIndex, shortValue);
    }


    public void updateShort(String columnLable, short shortValue) throws SQLException
    {
        this.passthru.updateShort(columnLable, shortValue);
    }


    public void updateInt(int columnIndex, int intValue) throws SQLException
    {
        this.passthru.updateInt(columnIndex, intValue);
    }


    public void updateInt(String columnLable, int intValue) throws SQLException
    {
        this.passthru.updateInt(columnLable, intValue);
    }


    public void updateLong(int columnIndex, long longValue) throws SQLException
    {
        this.passthru.updateLong(columnIndex, longValue);
    }


    public void updateLong(String columnLable, long longValue) throws SQLException
    {
        this.passthru.updateLong(columnLable, longValue);
    }


    public void updateFloat(String columnLable, float floatValue) throws SQLException
    {
        this.passthru.updateFloat(columnLable, floatValue);
    }


    public void updateFloat(int columnIndex, float floatValue) throws SQLException
    {
        this.passthru.updateFloat(columnIndex, floatValue);
    }


    public void updateDouble(int columnIndex, double doubleValue) throws SQLException
    {
        this.passthru.updateDouble(columnIndex, doubleValue);
    }


    public void updateDouble(String columnLable, double doubleValue) throws SQLException
    {
        this.passthru.updateDouble(columnLable, doubleValue);
    }


    public void updateBigDecimal(String columnLable, BigDecimal bigDecimalValue) throws SQLException
    {
        this.passthru.updateBigDecimal(columnLable, bigDecimalValue);
    }


    public void updateBigDecimal(int columnIndex, BigDecimal bigDecimalValue) throws SQLException
    {
        this.passthru.updateBigDecimal(columnIndex, bigDecimalValue);
    }


    public void updateString(String columnLable, String stringValue) throws SQLException
    {
        this.passthru.updateString(columnLable, stringValue);
    }


    public void updateString(int columnIndex, String stringValue) throws SQLException
    {
        this.passthru.updateString(columnIndex, stringValue);
    }


    public void updateBytes(int columnIndex, byte[] byteArrayValues) throws SQLException
    {
        this.passthru.updateBytes(columnIndex, byteArrayValues);
    }


    public void updateBytes(String columnLable, byte[] byteArrayValues) throws SQLException
    {
        this.passthru.updateBytes(columnLable, byteArrayValues);
    }


    public void updateDate(int columnIndex, Date dateValue) throws SQLException
    {
        this.passthru.updateDate(columnIndex, dateValue);
    }


    public void updateDate(String columnLable, Date dateValue) throws SQLException
    {
        this.passthru.updateDate(columnLable, dateValue);
    }


    public void updateTime(String columnLable, Time timeValue) throws SQLException
    {
        this.passthru.updateTime(columnLable, timeValue);
    }


    public void updateTime(int columnIndex, Time timeValue) throws SQLException
    {
        this.passthru.updateTime(columnIndex, timeValue);
    }


    public void updateTimestamp(int columnIndex, Timestamp timestampValue) throws SQLException
    {
        this.passthru.updateTimestamp(columnIndex, timestampValue);
    }


    public void updateTimestamp(String columnLable, Timestamp timestampValue) throws SQLException
    {
        this.passthru.updateTimestamp(columnLable, timestampValue);
    }


    public void updateAsciiStream(int columnIndex, InputStream inputStream, int length) throws SQLException
    {
        this.passthru.updateAsciiStream(columnIndex, inputStream, length);
    }


    public void updateAsciiStream(String columnLable, InputStream inputStream, int length) throws SQLException
    {
        this.passthru.updateAsciiStream(columnLable, inputStream, length);
    }


    public void updateBinaryStream(int columnIndex, InputStream inputStream, int length) throws SQLException
    {
        this.passthru.updateBinaryStream(columnIndex, inputStream, length);
    }


    public void updateBinaryStream(String columnLable, InputStream inputStream, int length) throws SQLException
    {
        this.passthru.updateBinaryStream(columnLable, inputStream, length);
    }


    public void updateCharacterStream(int columnIndex, Reader reader, int length) throws SQLException
    {
        this.passthru.updateCharacterStream(columnIndex, reader, length);
    }


    public void updateCharacterStream(String columnIndex, Reader reader, int length) throws SQLException
    {
        this.passthru.updateCharacterStream(columnIndex, reader, length);
    }


    public void updateObject(int columnIndex, Object object) throws SQLException
    {
        this.passthru.updateObject(columnIndex, object);
    }


    public void updateObject(int columnIndex, Object object, int scaleOrLength) throws SQLException
    {
        this.passthru.updateObject(columnIndex, object, scaleOrLength);
    }


    public void updateObject(String columnLable, Object object) throws SQLException
    {
        this.passthru.updateObject(columnLable, object);
    }


    public void updateObject(String columnLable, Object object, int scaleOrLength) throws SQLException
    {
        this.passthru.updateObject(columnLable, object, scaleOrLength);
    }


    public void insertRow() throws SQLException
    {
        this.passthru.insertRow();
    }


    public void updateRow() throws SQLException
    {
        this.passthru.updateRow();
    }


    public void deleteRow() throws SQLException
    {
        this.passthru.deleteRow();
    }


    public void refreshRow() throws SQLException
    {
        this.passthru.refreshRow();
    }


    public void cancelRowUpdates() throws SQLException
    {
        this.passthru.cancelRowUpdates();
    }


    public void moveToInsertRow() throws SQLException
    {
        this.passthru.moveToInsertRow();
    }


    public void moveToCurrentRow() throws SQLException
    {
        this.passthru.moveToCurrentRow();
    }


    public Blob getBlob(int columnIndex) throws SQLException
    {
        return this.passthru.getBlob(columnIndex);
    }


    public Blob getBlob(String columnLable) throws SQLException
    {
        return this.passthru.getBlob(columnLable);
    }


    public Clob getClob(String columnLable) throws SQLException
    {
        return this.passthru.getClob(columnLable);
    }


    public Clob getClob(int columnIndex) throws SQLException
    {
        return this.passthru.getClob(columnIndex);
    }


    public Array getArray(int columnIndex) throws SQLException
    {
        return this.passthru.getArray(columnIndex);
    }


    public Array getArray(String columnLable) throws SQLException
    {
        return this.passthru.getArray(columnLable);
    }


    public URL getURL(int columnIndex) throws SQLException
    {
        return this.passthru.getURL(columnIndex);
    }


    public URL getURL(String columnLable) throws SQLException
    {
        return this.passthru.getURL(columnLable);
    }


    public void updateRef(int columnIndex, Ref ref) throws SQLException
    {
        this.passthru.updateRef(columnIndex, ref);
    }


    public void updateRef(String columnLable, Ref ref) throws SQLException
    {
        this.passthru.updateRef(columnLable, ref);
    }


    public void updateBlob(int columnIndex, Blob blob) throws SQLException
    {
        this.passthru.updateBlob(columnIndex, blob);
    }


    public void updateBlob(String columnLable, Blob blob) throws SQLException
    {
        this.passthru.updateBlob(columnLable, blob);
    }


    public void updateClob(int columnIndex, Clob clob) throws SQLException
    {
        this.passthru.updateClob(columnIndex, clob);
    }


    public void updateClob(String columnLable, Clob clob) throws SQLException
    {
        this.passthru.updateClob(columnLable, clob);
    }


    public void updateArray(int columnIndex, Array array) throws SQLException
    {
        this.passthru.updateArray(columnIndex, array);
    }


    public void updateArray(String columnLable, Array array) throws SQLException
    {
        this.passthru.updateArray(columnLable, array);
    }


    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException
    {
        return this.passthru.getObject(columnIndex, map);
    }


    public Object getObject(String columnLable, Map<String, Class<?>> map) throws SQLException
    {
        return this.passthru.getObject(columnLable, map);
    }


    public BigDecimal getBigDecimal(int columnIndex, int bigDecimalValue) throws SQLException
    {
        return this.passthru.getBigDecimal(columnIndex, bigDecimalValue);
    }


    public BigDecimal getBigDecimal(String columnLable, int bigDecimalValue) throws SQLException
    {
        return this.passthru.getBigDecimal(columnLable, bigDecimalValue);
    }


    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return this.passthru.getUnicodeStream(columnIndex);
    }


    public InputStream getUnicodeStream(String columnLable) throws SQLException
    {
        return this.passthru.getUnicodeStream(columnLable);
    }


    public int getHoldability() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Reader getNCharacterStream(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Reader getNCharacterStream(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public NClob getNClob(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public NClob getNClob(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public String getNString(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public String getNString(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public RowId getRowId(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public RowId getRowId(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public SQLXML getSQLXML(int arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public SQLXML getSQLXML(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isClosed() throws SQLException
    {
        try
        {
            return this.passthru.isClosed();
        }
        catch(AbstractMethodError e)
        {
            return this.closed;
        }
    }


    public void updateNClob(int arg0, NClob arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNClob(String arg0, NClob arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNString(int arg0, String arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNString(String arg0, String arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateRowId(int arg0, RowId arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateRowId(String arg0, RowId arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isWrapperFor(Class<?> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateBlob(String arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateClob(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateClob(String arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNClob(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void updateNClob(String arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T unwrap(Class<T> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException
    {
        throw new UnsupportedOperationException();
    }
}
