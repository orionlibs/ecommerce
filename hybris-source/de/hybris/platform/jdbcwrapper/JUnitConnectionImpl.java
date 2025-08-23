package de.hybris.platform.jdbcwrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;

public class JUnitConnectionImpl extends ConnectionImpl
{
    public static final String PREPARE_ERROR_QUERY = "TEST:throw.error.on prepare";
    private volatile boolean forceHasError = false;
    private volatile CommitMode commitMode = CommitMode.NORMAL;
    private volatile boolean hasBeenDestroyed = false;


    public JUnitConnectionImpl(HybrisDataSource ds, Connection conn)
    {
        super(ds, conn);
    }


    public void setError(boolean hasError)
    {
        this.forceHasError = hasError;
    }


    public void setCommitMode(CommitMode mode)
    {
        this.commitMode = mode;
    }


    public void commit() throws SQLException
    {
        switch(null.$SwitchMap$de$hybris$platform$jdbcwrapper$JUnitConnectionImpl$CommitMode[this.commitMode.ordinal()])
        {
            case 1:
                super.commit();
                throw new SQLTransactionRollbackException("Transaction rolled back as requested by test mode " + this.commitMode);
            case 2:
                throw new SQLTransactionRollbackException("Transaction rolled back as requested by test mode " + this.commitMode);
            case 3:
                rollback();
                throw new SQLTransactionRollbackException("Transaction rolled back as requested by test mode " + this.commitMode);
        }
        super.commit();
    }


    void destroy() throws SQLException
    {
        try
        {
            super.destroy();
        }
        finally
        {
            this.hasBeenDestroyed = true;
        }
    }


    public boolean hasBeenDestroyed()
    {
        return this.hasBeenDestroyed;
    }


    public String parseQuery(String queryIn) throws SQLException
    {
        if("TEST:throw.error.on prepare".equalsIgnoreCase(queryIn))
        {
            throw new SQLException("test error on preare - as requested");
        }
        return queryIn;
    }


    protected void autoRollbackOnUnsetTxBOund()
    {
        throw new IllegalStateException("JUnitConnectionImpl doesnt automatically rollback open transactions!");
    }


    protected boolean gotError()
    {
        return (this.forceHasError || super.gotError());
    }


    public void resetTestMode()
    {
        this.forceHasError = false;
        this.commitMode = CommitMode.NORMAL;
    }
}
