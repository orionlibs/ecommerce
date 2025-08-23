package de.hybris.platform.jdbcwrapper.interceptor.recover;

import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptor;
import de.hybris.platform.jdbcwrapper.interceptor.JDBCInterceptorContext;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SQLRecoverableExceptionHandler implements JDBCInterceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(SQLRecoverableExceptionHandler.class);
    private final ThreadLocal<Boolean> probingRightNow = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private final SQLRecoveryStrategy recoveryStrategy;
    private final AtomicReference<State> state = new AtomicReference<>(State.createOpen());


    public SQLRecoverableExceptionHandler(SQLRecoveryStrategy recoveryStrategy)
    {
        this.recoveryStrategy = Objects.<SQLRecoveryStrategy>requireNonNull(recoveryStrategy);
    }


    private static void logChange(State from, State to)
    {
        Duration duration = Duration.between(from.getInstant(), to.getInstant());
        LOG.info("Changing state from '{}' to '{}'. Δ={}", new Object[] {from, to, duration});
    }


    public <T> T get(JDBCInterceptorContext ctx, JDBCInterceptor.SupplierWithSQLException<T> supplier) throws SQLException
    {
        Objects.requireNonNull(ctx, "ctx mustn't be null.");
        Objects.requireNonNull(supplier, "supplier mustn't be null.");
        if(isProbingInProgress())
        {
            return (T)supplier.get();
        }
        State current = this.state.get();
        if(current.isOpen())
        {
            return passThrough(supplier);
        }
        Duration backoffDuration = calculateBackoffDuration(current.getClosedIterations());
        if(!canRecover(ctx) || !current.hasBeenClosedForAtLeast(backoffDuration))
        {
            LOG.debug("Recovery in progress. Please try again later.");
            throw new RecoveryInProgressException();
        }
        State stillClosed = tryToBecomeTheOneWhoWillProbeTheDatabase(current);
        if(stillClosed == null)
        {
            return get(ctx, supplier);
        }
        return probeTheDatabase(supplier);
    }


    public boolean isOpen()
    {
        return ((State)this.state.get()).isOpen();
    }


    public final void forceClose()
    {
        force(true);
    }


    public final void forceOpen()
    {
        force(false);
    }


    private boolean canRecover(JDBCInterceptorContext ctx)
    {
        return this.recoveryStrategy.canRecover(ctx);
    }


    private <T> T passThrough(JDBCInterceptor.SupplierWithSQLException<T> s) throws SQLException
    {
        try
        {
            return (T)s.get();
        }
        catch(SQLException e)
        {
            if(isRecoverable(e))
            {
                LOG.debug("Recoverable exception ('{}' ErrorCode: {}) detected during passing through.", e.getClass().getName(),
                                Integer.valueOf(e.getErrorCode()));
                forceClose();
            }
            throw e;
        }
    }


    private <T> T probeTheDatabase(JDBCInterceptor.SupplierWithSQLException<T> s) throws SQLException
    {
        boolean firstCallToProbeInThisThread = markAsProbing();
        State current = this.state.get();
        try
        {
            T result = (T)s.get();
            LOG.info("Probing finished without exception.");
            tryToOpen(current);
            return result;
        }
        catch(SQLException e)
        {
            if(isRecoverable(e))
            {
                LOG.info("Recoverable exception ('{}' ErrorCode: {}) detected during probing.", e.getClass().getName(),
                                Integer.valueOf(e.getErrorCode()));
                forceClose();
            }
            throw e;
        }
        finally
        {
            resetProbing(firstCallToProbeInThisThread);
        }
    }


    private boolean isProbingInProgress()
    {
        return ((Boolean)this.probingRightNow.get()).booleanValue();
    }


    private boolean markAsProbing()
    {
        Boolean prev = this.probingRightNow.get();
        this.probingRightNow.set(Boolean.TRUE);
        return !prev.booleanValue();
    }


    private void resetProbing(boolean firstCallToProbleInThisThread)
    {
        if(firstCallToProbleInThisThread)
        {
            this.probingRightNow.set(Boolean.FALSE);
        }
    }


    private Duration calculateBackoffDuration(int numberOfIterations)
    {
        return this.recoveryStrategy.calculateBackoffDuration(numberOfIterations);
    }


    private boolean isRecoverable(SQLException e)
    {
        return this.recoveryStrategy.isRecoverable(e);
    }


    private void force(boolean closed)
    {
        State current, forced;
        do
        {
            current = this.state.get();
            if(closed == current.isClosed())
            {
                return;
            }
            forced = closed ? State.createClosed() : State.createOpen();
        }
        while(!this.state.compareAndSet(current, forced));
        logChange(current, forced);
    }


    private void tryToOpen(State current)
    {
        if(current.isOpen())
        {
            return;
        }
        State open = State.createOpen();
        if(this.state.compareAndSet(current, open))
        {
            logChange(current, open);
        }
    }


    private State tryToBecomeTheOneWhoWillProbeTheDatabase(State current)
    {
        State stillClosed = current.createStillClosed();
        if(this.state.compareAndSet(current, stillClosed))
        {
            logChange(current, stillClosed);
            return stillClosed;
        }
        return null;
    }


    public String toString()
    {
        return ((State)this.state.get()).toString();
    }
}
