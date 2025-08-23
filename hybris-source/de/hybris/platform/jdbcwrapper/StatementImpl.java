package de.hybris.platform.jdbcwrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class StatementImpl extends WrapperRelease<ResultSet> implements Statement
{
    private static final Logger LOG = Logger.getLogger(StatementImpl.class.getName());
    private final boolean doLog;
    private final boolean doAppend;
    private final boolean isClosedMethodSupported;
    private final Statement passthru;
    protected final ConnectionImpl connection;
    protected ArrayList<String> batchStatementsToLog;
    private volatile boolean isClosedFlag = false;


    public StatementImpl(ConnectionImpl conn, Statement statement)
    {
        this.passthru = statement;
        this.connection = conn;
        this.isClosedMethodSupported = checkIsCloseMethodSupported(statement);
        this.doLog = this.connection.getDataSource().getLogUtils().isLoggingActivated();
        this.doAppend = this.connection.getDataSource().getLogUtils().isStackAppendingActivated();
    }


    private boolean checkIsCloseMethodSupported(Statement rawStatement)
    {
        if(this.connection.getDataSource()
                        .getDatabaseName() == "sap" && rawStatement instanceof java.sql.PreparedStatement)
        {
            return false;
        }
        try
        {
            rawStatement.isClosed();
        }
        catch(LinkageError e)
        {
            return false;
        }
        catch(SQLException e)
        {
            LOG.error("unexpected error testing isClosed() on " + rawStatement, e);
        }
        return true;
    }


    public ConnectionImpl getConnection() throws SQLException
    {
        return this.connection;
    }


    protected ResultSet wrapResultSet(ResultSet real) throws SQLException
    {
        if(real != null)
        {
            ResultSet createdResultSet = getConnection().getDataSource().getDataSourceFactory().wrapResultSet(this, real);
            addResource(createdResultSet);
            return createdResultSet;
        }
        return null;
    }


    protected void releaseResourceImpl(ResultSet resource) throws SQLException
    {
        if(resource != null && !resource.isClosed())
        {
            resource.close();
        }
    }


    protected boolean isFlexibleSyntax(String query) throws SQLException
    {
        return this.connection.isFlexibleSyntax(query);
    }


    public ResultSet executeQuery(String query) throws SQLException
    {
        getConnection().protectReadOnly(query);
        if(this.doAppend)
        {
            query = query + query;
        }
        if(this.doLog)
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return wrapResultSet(this.passthru.executeQuery(query));
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing query");
                throw e;
            }
            finally
            {
                this.connection
                                .getDataSource()
                                .getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), this.connection.getConnectionID(), startTime, "statement", "", query);
            }
        }
        try
        {
            return wrapResultSet(this.passthru.executeQuery(query));
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing query");
            throw e;
        }
    }


    public int executeUpdate(String query) throws SQLException
    {
        getConnection().assureWritable();
        if(this.doAppend)
        {
            query = query + query;
        }
        if(this.doLog)
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.passthru.executeUpdate(query);
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing update");
                throw e;
            }
            finally
            {
                this.connection
                                .getDataSource()
                                .getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), this.connection.getConnectionID(), startTime, "statement", "", query);
            }
        }
        try
        {
            return this.passthru.executeUpdate(query);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing update");
            throw e;
        }
    }


    public boolean execute(String query) throws SQLException
    {
        getConnection().protectReadOnly(query);
        if(this.doAppend)
        {
            query = query + query;
        }
        if(this.doLog)
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.passthru.execute(query);
            }
            catch(SQLException e)
            {
                getConnection().notifyError(e);
                getConnection().logError(e, "error executing statement");
                throw e;
            }
            finally
            {
                this.connection
                                .getDataSource()
                                .getLogUtils()
                                .logElapsed(Thread.currentThread().getId(), this.connection.getConnectionID(), startTime, "statement", "", query);
            }
        }
        try
        {
            return this.passthru.execute(query);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public ResultSet getResultSet() throws SQLException
    {
        return wrapResultSet(this.passthru.getResultSet());
    }


    public ResultSet getGeneratedKeys() throws SQLException
    {
        return this.passthru.getGeneratedKeys();
    }


    protected void notifyResultSetClosed(ResultSetImpl resultSet)
    {
        removeResource(resultSet);
    }


    public void close() throws SQLException
    {
        try
        {
            releaseResources();
        }
        finally
        {
            this.passthru.close();
            if(!this.isClosedMethodSupported)
            {
                this.isClosedFlag = true;
            }
            this.connection.notifyStatementClosed(this);
        }
    }


    public int getMaxFieldSize() throws SQLException
    {
        return this.passthru.getMaxFieldSize();
    }


    public void setMaxFieldSize(int size) throws SQLException
    {
        this.passthru.setMaxFieldSize(size);
    }


    public int getMaxRows() throws SQLException
    {
        return this.passthru.getMaxRows();
    }


    public void setMaxRows(int rows) throws SQLException
    {
        this.passthru.setMaxRows(rows);
    }


    public void setEscapeProcessing(boolean enable) throws SQLException
    {
        this.passthru.setEscapeProcessing(enable);
    }


    public int getQueryTimeout() throws SQLException
    {
        return this.passthru.getQueryTimeout();
    }


    public void setQueryTimeout(int seconds) throws SQLException
    {
        this.passthru.setQueryTimeout(seconds);
    }


    public void cancel() throws SQLException
    {
        this.passthru.cancel();
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return this.passthru.getWarnings();
    }


    public void clearWarnings() throws SQLException
    {
        this.passthru.clearWarnings();
    }


    public void setCursorName(String name) throws SQLException
    {
        this.passthru.setCursorName(name);
    }


    public int getUpdateCount() throws SQLException
    {
        return this.passthru.getUpdateCount();
    }


    public boolean getMoreResults() throws SQLException
    {
        return this.passthru.getMoreResults();
    }


    public void setFetchDirection(int direction) throws SQLException
    {
        this.passthru.setFetchDirection(direction);
    }


    public int getFetchDirection() throws SQLException
    {
        return this.passthru.getFetchDirection();
    }


    public void setFetchSize(int size) throws SQLException
    {
        this.passthru.setFetchSize(size);
    }


    public int getFetchSize() throws SQLException
    {
        return this.passthru.getFetchSize();
    }


    public int getResultSetConcurrency() throws SQLException
    {
        return this.passthru.getResultSetConcurrency();
    }


    public int getResultSetType() throws SQLException
    {
        return this.passthru.getResultSetType();
    }


    public void addBatch(String query) throws SQLException
    {
        getConnection().assureWritable();
        this.passthru.addBatch(query);
        if(this.doLog)
        {
            addToLogBatchStatements(query);
        }
    }


    protected void addToLogBatchStatements(String query)
    {
        if(this.batchStatementsToLog == null)
        {
            this.batchStatementsToLog = new ArrayList<>();
        }
        this.batchStatementsToLog.add(query);
    }


    public void clearBatch() throws SQLException
    {
        this.passthru.clearBatch();
        if(this.doLog && CollectionUtils.isNotEmpty(this.batchStatementsToLog))
        {
            this.batchStatementsToLog.clear();
        }
    }


    public int[] executeBatch() throws SQLException
    {
        getConnection().assureWritable();
        if(this.doLog)
        {
            long startTime = System.currentTimeMillis();
            try
            {
                return this.passthru.executeBatch();
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
                    this.connection
                                    .getDataSource()
                                    .getLogUtils()
                                    .logElapsed(Thread.currentThread().getId(), this.connection.getConnectionID(), startTime, "statement", "", query);
                }
                this.batchStatementsToLog.clear();
            }
        }
        try
        {
            return this.passthru.executeBatch();
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing batch");
            throw e;
        }
    }


    public boolean getMoreResults(int current) throws SQLException
    {
        return this.passthru.getMoreResults(current);
    }


    public int executeUpdate(String query, int autoGeneratedKeys) throws SQLException
    {
        getConnection().assureWritable();
        try
        {
            return this.passthru.executeUpdate(query, autoGeneratedKeys);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing update");
            throw e;
        }
    }


    public int executeUpdate(String query, int[] columnIndexes) throws SQLException
    {
        getConnection().assureWritable();
        try
        {
            return this.passthru.executeUpdate(query, columnIndexes);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing update");
            throw e;
        }
    }


    public int executeUpdate(String query, String[] columnNames) throws SQLException
    {
        getConnection().assureWritable();
        try
        {
            return this.passthru.executeUpdate(query, columnNames);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public boolean execute(String query, int autoGeneratedKeys) throws SQLException
    {
        getConnection().protectReadOnly(query);
        try
        {
            return this.passthru.execute(query, autoGeneratedKeys);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public boolean execute(String query, int[] columnIndexes) throws SQLException
    {
        getConnection().protectReadOnly(query);
        try
        {
            return this.passthru.execute(query, columnIndexes);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public boolean execute(String query, String[] columnNames) throws SQLException
    {
        getConnection().protectReadOnly(query);
        try
        {
            return this.passthru.execute(query, columnNames);
        }
        catch(SQLException e)
        {
            getConnection().notifyError(e);
            getConnection().logError(e, "error executing statement");
            throw e;
        }
    }


    public int getResultSetHoldability() throws SQLException
    {
        return this.passthru.getResultSetHoldability();
    }


    public <T> T unwrap(Class<T> arg0) throws SQLException
    {
        return (T)this.passthru;
    }


    public boolean isWrapperFor(Class<?> arg0) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public void setPoolable(boolean poolable) throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isPoolable() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isClosed() throws SQLException
    {
        return this.isClosedMethodSupported ? this.passthru.isClosed() : this.isClosedFlag;
    }


    public void closeOnCompletion() throws SQLException
    {
        throw new UnsupportedOperationException();
    }


    public boolean isCloseOnCompletion() throws SQLException
    {
        throw new UnsupportedOperationException();
    }
}
