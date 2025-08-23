package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.util.Utilities;
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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class ConnectionImpl extends WrapperRelease<Statement> implements Connection, ParsedStatement
{
    private static final Logger LOG = Logger.getLogger(ConnectionImpl.class.getName());
    private static final AtomicInteger maxID = new AtomicInteger(0);
    private final HybrisDataSource dataSource;
    private final Connection conn;
    private final int connID;
    private final ActiveConnectionSettings activeSettings = new ActiveConnectionSettings();
    private ErrorHandler errorHandler;
    private volatile long errorCounter;


    public ConnectionImpl(HybrisDataSource dataSource, Connection connection)
    {
        if(connection == null)
        {
            throw new IllegalArgumentException("connection was null");
        }
        if(dataSource == null)
        {
            throw new IllegalArgumentException("data source was null");
        }
        this.conn = connection;
        this.dataSource = dataSource;
        this.connID = nextId();
        this.errorCounter = 0L;
    }


    private static int nextId()
    {
        int next = maxID.getAndIncrement();
        if(next == Integer.MAX_VALUE)
        {
            maxID.set(0);
        }
        return next;
    }


    protected boolean gotError()
    {
        return this.activeSettings.hasError();
    }


    protected boolean hasBeenRollbacked()
    {
        return this.activeSettings.hasBeenRollbacked();
    }


    protected void notifyError(SQLException cause)
    {
        boolean isConnectionStillValid = isUnderlyingConnectionValid();
        if(this.errorHandler != null && !isConnectionStillValid)
        {
            this.errorHandler.onError(cause.getMessage(), cause);
        }
        this.activeSettings.error();
    }


    protected void logError(SQLException e, String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(getConnectionInfo() + ":" + getConnectionInfo(), e);
        }
    }


    protected String getConnectionInfo()
    {
        boolean closed = false;
        try
        {
            closed = isClosed();
        }
        catch(SQLException sQLException)
        {
        }
        return "[connection:" + getConnectionID() + ",txBound:" + isTxBound() + ",closed:" + closed + "]";
    }


    protected void protectReadOnly(String sql) throws SQLException
    {
        if(getDataSource().isReadOnly())
        {
            String quer = trimQuery4Check(sql);
            if(!quer.startsWith("select") && !quer.startsWith("show"))
            {
                throw new SQLException("data source " + getDataSource() + " is read-only - cannot execute '" + sql + "'");
            }
        }
    }


    private String trimQuery4Check(String query)
    {
        return trimQuery4Check(query, 10);
    }


    private String trimQuery4Check(String query, int length)
    {
        StringBuilder queryBuilder = new StringBuilder();
        int queryLength = query.length();
        for(int i = 0; i < queryLength && i < length; i++)
        {
            char character = query.charAt(i);
            if(!Character.isWhitespace(character) || queryBuilder.length() != 0)
            {
                queryBuilder.append(Character.toLowerCase(character));
            }
        }
        return queryBuilder.toString();
    }


    protected void assureWritable() throws SQLException
    {
        if(getDataSource().isReadOnly())
        {
            throw new SQLException("data source " + getDataSource() + " is read-only");
        }
    }


    public HybrisDataSource getDataSource()
    {
        return this.dataSource;
    }


    public void invalidate()
    {
        this.dataSource.invalidate(this);
    }


    public void unsetTxBound()
    {
        if(this.activeSettings.isTxBound())
        {
            if(!this.activeSettings.isAutoCommit() && !this.activeSettings.transactionHasEnded())
            {
                autoRollbackOnUnsetTxBOund();
            }
            if(this.activeSettings.getPreviousTxIsolationLevel() != -1)
            {
                restoreTxIsolationLevel();
            }
            if(!this.activeSettings.isAutoCommit())
            {
                restoreAutoCommit();
            }
        }
        this.activeSettings.unsetTxBound();
    }


    protected void restoreAutoCommit()
    {
        try
        {
            doSetAutoCommit(true);
        }
        catch(SQLException e)
        {
            LOG.error("error resetting AutoCommit", e);
        }
    }


    protected void restoreTxIsolationLevel()
    {
        try
        {
            doSetTransactionIsolation(this.activeSettings.getPreviousTxIsolationLevel());
        }
        catch(SQLException e)
        {
            LOG.error("error resetting isolation level", e);
        }
    }


    protected void autoRollbackOnUnsetTxBOund()
    {
        LOG.error("transaction of unbinding connection " + this + " has not been ended - will rollback automatically");
        try
        {
            rollback();
        }
        catch(Exception e)
        {
            LOG.error("error automatically rolling back open transaction of connection " + this + " : " + e.getMessage(), e);
        }
    }


    public void setTxBoundNoUserTA()
    {
        this.activeSettings.txBound();
    }


    public void setTxBoundUserTA(Integer isolationLevel) throws SQLException
    {
        doSetAutoCommit(false);
        int previousLevel = this.activeSettings.previousTxIsolationLevel;
        if(isolationLevel != null)
        {
            previousLevel = doSetTransactionIsolation(isolationLevel.intValue());
        }
        this.activeSettings.txBoundUserTA(previousLevel);
    }


    public boolean isTxBound()
    {
        return this.activeSettings.isTxBound();
    }


    public boolean isTxBoundUserTA()
    {
        return (this.activeSettings.isTxBound() && !this.activeSettings.isAutoCommit());
    }


    public boolean isClosed() throws SQLException
    {
        return (this.activeSettings.isClosed() || getUnderlayingConnection().isClosed());
    }


    public void prepareForUse()
    {
        this.activeSettings.prepareForUse();
    }


    public void restoreAfterUse() throws SQLException
    {
        if(!this.activeSettings.isAutoCommit())
        {
            if(!this.activeSettings.transactionHasEnded())
            {
                if(getUnderlayingConnection().getAutoCommit())
                {
                    LOG.warn("auto-commit state of " + this + " mismatch: active settings=false <> connection=true");
                }
                else
                {
                    autoRollbackOnUnsetTxBOund();
                }
            }
            setAutoCommit(true);
        }
        if(this.activeSettings.previousTxIsolationLevel != -1)
        {
            doSetTransactionIsolation(this.activeSettings.previousTxIsolationLevel);
        }
        this.activeSettings.afterUse();
    }


    public void closeUnderlayingConnection() throws SQLException
    {
        getUnderlayingConnection().close();
    }


    public Connection getUnderlayingConnection()
    {
        return this.conn;
    }


    public int getConnectionID()
    {
        return this.connID;
    }


    private Statement wrapStatement(Statement stmt)
    {
        Statement ret = getDataSource().getDataSourceFactory().wrapStatement(this, stmt);
        addResource(ret);
        return ret;
    }


    protected PreparedStatement wrapPreparedStatement(PreparedStatement pstmt, String sql)
    {
        PreparedStatement ret = getDataSource().getDataSourceFactory().wrapPreparedStatement(this, pstmt, sql);
        addResource(ret);
        return ret;
    }


    protected boolean isFlexibleSyntax(String query) throws SQLException
    {
        String lowQuery = query.toLowerCase();
        int fromIdx = lowQuery.indexOf("from");
        if(fromIdx > 0)
        {
            String str = trimQuery4Check(lowQuery.substring(fromIdx), 10);
            return (str.indexOf('{') > 0 || str.indexOf('}') > 0);
        }
        String queryStart = trimQuery4Check(query, 20);
        return (queryStart.indexOf('{') > 0 || queryStart.indexOf('}') > 0);
    }


    public void close() throws SQLException
    {
        if(!isTxBound())
        {
            if(this.activeSettings.isClosed())
            {
                LOG.warn("connection " + this + " is already closed (thread:" + Thread.currentThread() + ")");
            }
            this.activeSettings.close();
            try
            {
                releaseResources();
            }
            finally
            {
                this.dataSource.returnToPool(this);
            }
        }
    }


    void destroy() throws SQLException
    {
        try
        {
            restoreAfterUse();
        }
        finally
        {
            closeUnderlayingConnection();
        }
    }


    protected void notifyStatementClosed(StatementImpl stmt)
    {
        removeResource(stmt);
    }


    protected void releaseResourceImpl(Statement resource) throws SQLException
    {
        if(resource != null)
        {
            resource.close();
        }
    }


    public String parseQuery(String queryIn) throws SQLException
    {
        return queryIn;
    }


    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        this.activeSettings.autoCommit(doSetAutoCommit(autoCommit));
    }


    private final boolean doSetAutoCommit(boolean autoCommit) throws SQLException
    {
        boolean previous = getAutoCommit();
        if(previous != autoCommit)
        {
            try
            {
                getUnderlayingConnection().setAutoCommit(autoCommit);
            }
            catch(SQLException e)
            {
                throw handleSqlException(e, "error setting autoCommit");
            }
        }
        return autoCommit;
    }


    public boolean getAutoCommit() throws SQLException
    {
        return getUnderlayingConnection().getAutoCommit();
    }


    public void commit() throws SQLException
    {
        try
        {
            commitInternal();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error committing");
        }
    }


    protected void commitInternal() throws SQLException
    {
        if(this.dataSource.getLogUtils().isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                getUnderlayingConnection().commit();
            }
            finally
            {
                this.activeSettings.commit();
                String sql = "";
                if(this.dataSource.getLogUtils().isStackAppendingActivated())
                {
                    sql = Utilities.buildStackTraceCompact(new Exception());
                }
                this.dataSource.getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), getConnectionID(), startTime, "commit", sql, "");
            }
        }
        else
        {
            try
            {
                getUnderlayingConnection().commit();
            }
            finally
            {
                this.activeSettings.commit();
            }
        }
    }


    public void rollback() throws SQLException
    {
        try
        {
            rollbackInternal();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error rolling back");
        }
    }


    protected void rollbackInternal() throws SQLException
    {
        if(this.dataSource.getLogUtils().isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                getUnderlayingConnection().rollback();
            }
            finally
            {
                this.activeSettings.rollback();
                String sql = "";
                if(this.dataSource.getLogUtils().isStackAppendingActivated())
                {
                    sql = sql + sql;
                }
                this.dataSource.getLogUtils().logElapsed(Thread.currentThread().getId(), getConnectionID(), startTime, "rollback", sql, "");
            }
        }
        else
        {
            try
            {
                getUnderlayingConnection().rollback();
            }
            finally
            {
                this.activeSettings.rollback();
            }
        }
    }


    public void rollback(Savepoint savePoint) throws SQLException
    {
        if(this.dataSource.getLogUtils().isLoggingActivated())
        {
            long startTime = System.currentTimeMillis();
            try
            {
                getUnderlayingConnection().rollback(savePoint);
            }
            finally
            {
                String sql = "";
                if(this.dataSource.getLogUtils().isStackAppendingActivated())
                {
                    sql = sql + sql;
                }
                this.dataSource.getLogUtils().logElapsed(Thread.currentThread().getId(), getConnectionID(), startTime, "rollback", sql, "");
            }
        }
        else
        {
            getUnderlayingConnection().rollback(savePoint);
        }
    }


    public Statement createStatement() throws SQLException
    {
        try
        {
            return wrapStatement(getUnderlayingConnection().createStatement());
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error ceating statememt");
        }
    }


    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {
        try
        {
            return wrapStatement(getUnderlayingConnection().createStatement(resultSetType, resultSetConcurrency));
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error creating statememt");
        }
    }


    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        try
        {
            return wrapStatement(
                            getUnderlayingConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error creating statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(getUnderlayingConnection().prepareStatement(parsedQuery), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error prearing statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(
                            getUnderlayingConnection().prepareStatement(parsedQuery, resultSetType, resultSetConcurrency), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(getUnderlayingConnection().prepareStatement(parsedQuery, autoGeneratedKeys), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(getUnderlayingConnection().prepareStatement(parsedQuery, columnIndexes), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(getUnderlayingConnection().prepareStatement(parsedQuery, columnNames), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing statememt");
        }
    }


    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        try
        {
            String parsedQuery = parseQuery(sql);
            protectReadOnly(parsedQuery);
            return wrapPreparedStatement(getUnderlayingConnection().prepareStatement(parsedQuery, resultSetType, resultSetConcurrency, resultSetHoldability), parsedQuery);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing statememt");
        }
    }


    public CallableStatement prepareCall(String sql) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().prepareCall(sql);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing call");
        }
    }


    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing call");
        }
    }


    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error preparing call");
        }
    }


    public DatabaseMetaData getMetaData() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getMetaData();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting meta data");
        }
    }


    public String getCatalog() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getCatalog();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting catalog");
        }
    }


    public void setCatalog(String catalog) throws SQLException
    {
        try
        {
            getUnderlayingConnection().setCatalog(catalog);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting catalog");
        }
    }


    public int getHoldability() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getHoldability();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting holdability");
        }
    }


    public void setHoldability(int holdability) throws SQLException
    {
        try
        {
            getUnderlayingConnection().setHoldability(holdability);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting holdability");
        }
    }


    public int getTransactionIsolation() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getTransactionIsolation();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting tx isolation level");
        }
    }


    public void setTransactionIsolation(int level) throws SQLException
    {
        try
        {
            this.activeSettings.txIsolationLevel(doSetTransactionIsolation(level));
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting tx isolation level");
        }
    }


    private final int doSetTransactionIsolation(int level) throws SQLException
    {
        int previous = getTransactionIsolation();
        if(previous != level)
        {
            getUnderlayingConnection().setTransactionIsolation(level);
        }
        return previous;
    }


    private SQLException handleSqlException(SQLException e, String message)
    {
        notifyError(e);
        logError(e, message);
        if(this.errorHandler != null && !isUnderlyingConnectionValid())
        {
            this.errorHandler.onError(message, e);
        }
        return e;
    }


    public void markValid(long errorCounter)
    {
        this.errorCounter = errorCounter;
    }


    public long getLastValidationErrorCounter()
    {
        return this.errorCounter;
    }


    public Map getTypeMap() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getTypeMap();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting type map");
        }
    }


    public SQLWarning getWarnings() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().getWarnings();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting warnings");
        }
    }


    public void clearWarnings() throws SQLException
    {
        try
        {
            getUnderlayingConnection().clearWarnings();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error clearing warning");
        }
    }


    public boolean isReadOnly() throws SQLException
    {
        try
        {
            return (this.activeSettings.isReadOnly() || getUnderlayingConnection().isReadOnly());
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting read-only");
        }
    }


    public void setReadOnly(boolean readOnly) throws SQLException
    {
        try
        {
            getUnderlayingConnection().setReadOnly(readOnly);
            if(readOnly)
            {
                this.activeSettings.readOnly();
            }
            else
            {
                this.activeSettings.readWrite();
            }
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error set read-only");
        }
    }


    public Savepoint setSavepoint() throws SQLException
    {
        try
        {
            return getUnderlayingConnection().setSavepoint();
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting savepoint");
        }
    }


    public Savepoint setSavepoint(String name) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().setSavepoint(name);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting savepoint");
        }
    }


    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
        try
        {
            getUnderlayingConnection().releaseSavepoint(savepoint);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error releasing savepoint");
        }
    }


    public String nativeSQL(String sql) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().nativeSQL(sql);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error getting native sql");
        }
    }


    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException
    {
        try
        {
            getUnderlayingConnection().setTypeMap(arg0);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error setting type map");
        }
    }


    public Blob createBlob() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Clob createClob() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public NClob createNClob() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public SQLXML createSQLXML() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Properties getClientInfo() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public String getClientInfo(String arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isValid(int timeout) throws SQLException
    {
        try
        {
            return getUnderlayingConnection().isValid(timeout);
        }
        catch(SQLException e)
        {
            throw handleSqlException(e, "error checking is connection valid");
        }
    }


    public boolean isUnderlyingConnectionValid()
    {
        try
        {
            return getUnderlayingConnection().isValid(1);
        }
        catch(SQLException e)
        {
            return false;
        }
    }


    public boolean isWrapperFor(Class<?> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Array createArrayOf(String arg0, Object[] arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public Struct createStruct(String arg0, Object[] arg1) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setClientInfo(Properties arg0) throws SQLClientInfoException
    {
        throw new UnsupportedOperationException();
    }


    public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T unwrap(Class<T> arg0) throws SQLException
    {
        return (T)this.conn;
    }


    public void resetError()
    {
        this.activeSettings.noError();
    }


    public String toString()
    {
        return "" + getConnectionID() + "(" + getConnectionID() + ")";
    }


    public void setErrorHandler(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
    }


    public void setSchema(String schema) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public String getSchema() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void abort(Executor executor) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public int getNetworkTimeout() throws SQLException
    {
        throw new UnsupportedOperationException();
    }
}
