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
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;

public class PreparedStatementImpl extends StatementImpl implements PreparedStatement
{
    private static final int MAX_FIELDS = 32;
    private static final int GROW_MAX = 32;
    private static final String STATEMENT_INFO = "statement";
    private final boolean doLog;
    private final PreparedStatement prepStmtPassthru;
    private final String preparedQuery;
    private final String preparedQueryWithoutStackTrace;
    private Object[] values = null;
    private BitSet isString = null;


    public PreparedStatementImpl(ConnectionImpl conn, PreparedStatement statement, String query)
    {
        super(conn, statement);
        this.doLog = this.connection.getDataSource().getLogUtils().isLoggingActivated();
        this.preparedQueryWithoutStackTrace = query;
        if(this.doLog && this.connection.getDataSource().getLogUtils().isStackAppendingActivated())
        {
            this.preparedQuery = query + query;
        }
        else
        {
            this.preparedQuery = query;
        }
        this.prepStmtPassthru = statement;
    }


    public String getPreparedQueryWithoutStackTrace()
    {
        return this.preparedQueryWithoutStackTrace;
    }


    public String getPrepStmtPassthruString()
    {
        return this.prepStmtPassthru.toString();
    }


    protected boolean isLoggingActivated()
    {
        return this.doLog;
    }


    protected final String assembleQueryFromPreparedStatement()
    {
        int len = this.preparedQuery.length();
        StringBuilder stringBuilder = new StringBuilder(len * 2);
        if(this.values != null)
        {
            int index = 1, limit = 0, base = 0;
            while((limit = this.preparedQuery.indexOf('?', limit)) != -1)
            {
                if(this.isString.get(index))
                {
                    stringBuilder.append(this.preparedQuery.substring(base, limit));
                    stringBuilder.append("'");
                    stringBuilder.append(this.values[index]);
                    stringBuilder.append("'");
                }
                else
                {
                    stringBuilder.append(this.preparedQuery.substring(base, limit));
                    stringBuilder.append(this.values[index]);
                }
                index++;
                base = ++limit;
            }
            if(base < len)
            {
                stringBuilder.append(this.preparedQuery.substring(base));
            }
        }
        return stringBuilder.toString();
    }


    private void assureCapacity(int position)
    {
        if(this.values == null)
        {
            this.values = new Object[Math.max(position, 32) + 1];
            this.isString = new BitSet(Math.max(position, 32) + 1);
        }
        if(position >= this.values.length)
        {
            int size = this.values.length;
            Object[] valuesTmp = new Object[position + 32];
            System.arraycopy(this.values, 0, valuesTmp, 0, size);
            this.values = valuesTmp;
        }
    }


    private void logStringObject(int position, Object object)
    {
        if(position >= 0)
        {
            assureCapacity(position);
            this.values[position] = (object == null) ? "" : object.toString();
            this.isString.set(position);
        }
    }


    private void logOtherObject(int position, Object object)
    {
        if(position >= 0)
        {
            assureCapacity(position);
            this.values[position] = (object == null) ? "" : object.toString();
            this.isString.clear(position);
        }
    }


    public ResultSet getResultSet() throws SQLException
    {
        return wrapResultSet(this.prepStmtPassthru.getResultSet());
    }


    public ResultSet executeQuery() throws SQLException
    {
        if(isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return wrapResultSet(this.prepStmtPassthru.executeQuery());
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing query");
                throw e;
            }
            finally
            {
                this.connection.getDataSource().getLogUtils().logElapsed(Thread.currentThread().getId(), this.connection
                                                .getConnectionID(), startTime, "statement", this.preparedQueryWithoutStackTrace,
                                assembleQueryFromPreparedStatement());
            }
        }
        try
        {
            return wrapResultSet(this.prepStmtPassthru.executeQuery());
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing query");
            throw e;
        }
    }


    public boolean execute() throws SQLException
    {
        if(isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.prepStmtPassthru.execute();
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing statement");
                throw e;
            }
            finally
            {
                this.connection.getDataSource().getLogUtils().logElapsed(Thread.currentThread().getId(), this.connection
                                                .getConnectionID(), startTime, "statement", this.preparedQueryWithoutStackTrace,
                                assembleQueryFromPreparedStatement());
            }
        }
        try
        {
            return this.prepStmtPassthru.execute();
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public int[] executeBatch() throws SQLException
    {
        if(isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.prepStmtPassthru.executeBatch();
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing batch");
                throw e;
            }
            finally
            {
                for(String query : this.batchStatementsToLog)
                {
                    this.connection.getDataSource().getLogUtils().logElapsed(Thread.currentThread().getId(), this.connection
                                    .getConnectionID(), startTime, "statement", this.preparedQueryWithoutStackTrace, query);
                }
                this.batchStatementsToLog.clear();
            }
        }
        try
        {
            return this.prepStmtPassthru.executeBatch();
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing batch");
            throw e;
        }
    }


    public int executeUpdate() throws SQLException
    {
        if(isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.prepStmtPassthru.executeUpdate();
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing update");
                throw e;
            }
            finally
            {
                this.connection.getDataSource().getLogUtils().logElapsed(Thread.currentThread().getId(), this.connection
                                                .getConnectionID(), startTime, "statement", this.preparedQueryWithoutStackTrace,
                                assembleQueryFromPreparedStatement());
            }
        }
        try
        {
            return this.prepStmtPassthru.executeUpdate();
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing update");
            throw e;
        }
    }


    public void setArray(int parameterIndex, Array array) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, array);
        }
        this.prepStmtPassthru.setArray(parameterIndex, array);
    }


    public void setAsciiStream(int parameterIndex, InputStream inputStream, int length) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, inputStream);
        }
        this.prepStmtPassthru.setAsciiStream(parameterIndex, inputStream, length);
    }


    public void setBigDecimal(int parameterIndex, BigDecimal bigDecimalValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, bigDecimalValue);
        }
        this.prepStmtPassthru.setBigDecimal(parameterIndex, bigDecimalValue);
    }


    public void setBinaryStream(int parameterIndex, InputStream inputStream, int length) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, inputStream);
        }
        this.prepStmtPassthru.setBinaryStream(parameterIndex, inputStream, length);
    }


    public void setBlob(int parameterIndex, Blob blob) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, blob);
        }
        this.prepStmtPassthru.setBlob(parameterIndex, blob);
    }


    public void setBoolean(int parameterIndex, boolean booleanValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, Boolean.valueOf(booleanValue));
        }
        this.prepStmtPassthru.setBoolean(parameterIndex, booleanValue);
    }


    public void setByte(int parameterIndex, byte byteValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, Byte.valueOf(byteValue));
        }
        this.prepStmtPassthru.setByte(parameterIndex, byteValue);
    }


    public void setBytes(int parameterIndex, byte[] byteArray) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, byteArray);
        }
        this.prepStmtPassthru.setBytes(parameterIndex, byteArray);
    }


    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, reader);
        }
        this.prepStmtPassthru.setCharacterStream(parameterIndex, reader, length);
    }


    public void setClob(int parameterIndex, Clob clob) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, clob);
        }
        this.prepStmtPassthru.setClob(parameterIndex, clob);
    }


    public void setDate(int parameterIndex, Date date) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, date);
        }
        this.prepStmtPassthru.setDate(parameterIndex, date);
    }


    public void setDate(int parameterIndex, Date date, Calendar cal) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, date);
        }
        this.prepStmtPassthru.setDate(parameterIndex, date, cal);
    }


    public void setDouble(int parameterIndex, double doubleValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logOtherObject(parameterIndex, Double.valueOf(doubleValue));
        }
        this.prepStmtPassthru.setDouble(parameterIndex, doubleValue);
    }


    public void setFloat(int parameterIndex, float floatValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logOtherObject(parameterIndex, Float.valueOf(floatValue));
        }
        this.prepStmtPassthru.setFloat(parameterIndex, floatValue);
    }


    public void setInt(int parameterIndex, int intValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logOtherObject(parameterIndex, Integer.valueOf(intValue));
        }
        this.prepStmtPassthru.setInt(parameterIndex, intValue);
    }


    public void setLong(int parameterIndex, long longValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logOtherObject(parameterIndex, Long.valueOf(longValue));
        }
        this.prepStmtPassthru.setLong(parameterIndex, longValue);
    }


    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, null);
        }
        this.prepStmtPassthru.setNull(parameterIndex, sqlType, typeName);
    }


    public void setNull(int parameterIndex, int sqlType) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, null);
        }
        this.prepStmtPassthru.setNull(parameterIndex, sqlType);
    }


    public void setObject(int parameterIndex, Object object, int targetSqlType, int scaleOrLength) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, object);
        }
        this.prepStmtPassthru.setObject(parameterIndex, object, targetSqlType, scaleOrLength);
    }


    public void setObject(int parameterIndex, Object object, int targetSqlType) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, object);
        }
        this.prepStmtPassthru.setObject(parameterIndex, object, targetSqlType);
    }


    public void setObject(int parameterIndex, Object object) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, object);
        }
        this.prepStmtPassthru.setObject(parameterIndex, object);
    }


    public void setRef(int parameterIndex, Ref ref) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, ref);
        }
        this.prepStmtPassthru.setRef(parameterIndex, ref);
    }


    public void setShort(int parameterIndex, short shortValue) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, Short.valueOf(shortValue));
        }
        this.prepStmtPassthru.setShort(parameterIndex, shortValue);
    }


    public void setString(int parameterIndex, String value) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, value);
        }
        this.prepStmtPassthru.setString(parameterIndex, value);
    }


    public void setTime(int parameterIndex, Time time, Calendar cal) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, time);
        }
        this.prepStmtPassthru.setTime(parameterIndex, time, cal);
    }


    public void setTime(int parameterIndex, Time time) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, time);
        }
        this.prepStmtPassthru.setTime(parameterIndex, time);
    }


    public void setTimestamp(int parameterIndex, Timestamp timestamp, Calendar cal) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, timestamp);
        }
        this.prepStmtPassthru.setTimestamp(parameterIndex, timestamp, cal);
    }


    public void setTimestamp(int parameterIndex, Timestamp timestamp) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, timestamp);
        }
        this.prepStmtPassthru.setTimestamp(parameterIndex, timestamp);
    }


    public void setURL(int parameterIndex, URL url) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, url);
        }
        this.prepStmtPassthru.setURL(parameterIndex, url);
    }


    public void setUnicodeStream(int parameterIndex, InputStream inputStream, int length) throws SQLException
    {
        if(isLoggingActivated())
        {
            logStringObject(parameterIndex, inputStream);
        }
        this.prepStmtPassthru.setUnicodeStream(parameterIndex, inputStream, length);
    }


    public void addBatch() throws SQLException
    {
        this.prepStmtPassthru.addBatch();
        if(this.doLog)
        {
            addToLogBatchStatements(assembleQueryFromPreparedStatement());
        }
    }


    public void clearParameters() throws SQLException
    {
        this.prepStmtPassthru.clearParameters();
    }


    public ResultSetMetaData getMetaData() throws SQLException
    {
        return this.prepStmtPassthru.getMetaData();
    }


    public ParameterMetaData getParameterMetaData() throws SQLException
    {
        return this.prepStmtPassthru.getParameterMetaData();
    }


    public boolean isPoolable() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setBlob(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setClob(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNClob(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNClob(int arg0, NClob arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNString(int arg0, String arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setPoolable(boolean arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setRowId(int arg0, RowId arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setSQLXML(int arg0, SQLXML arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setBinaryStream(int arg0, InputStream arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setCharacterStream(int arg0, Reader arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }
}
