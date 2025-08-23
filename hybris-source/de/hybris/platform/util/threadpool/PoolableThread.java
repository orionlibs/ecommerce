package de.hybris.platform.util.threadpool;

import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import java.util.Queue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class PoolableThread extends RegistrableThread
{
    private static final Logger log = Logger.getLogger(PoolableThread.class.getName());
    private volatile Runnable runnable;
    private volatile boolean isStarted = false;
    private volatile boolean isInvalid = false;
    private volatile boolean isBorrowed = false;
    private String myName;
    private final ThreadPool pool;


    PoolableThread(ThreadPool pool)
    {
        this.pool = pool;
        String name = "PooledThread";
        if(this.pool.getTenantID() != null)
        {
            name = name + "<<" + name + ">>";
        }
        name = name + "[" + name + "]";
        setName(name);
        pool.registerForCleanup(this);
    }


    private synchronized void clearInvalidTransactionIfNeeded()
    {
        Transaction current = Transaction.current();
        if(current != null && current.isRunning())
        {
            reportStaleTransaction(current);
            do
            {
                try
                {
                    current.rollback();
                }
                catch(Exception e)
                {
                    log.error("Unable to rollback stale transaction for PoolableThread " + this, e);
                    break;
                }
                finally
                {
                    this.runnable = null;
                    if(this.isStarted)
                    {
                        this.isInvalid = true;
                    }
                }
            }
            while(current.isRunning());
        }
    }


    private void reportStaleTransaction(Transaction current)
    {
        try
        {
            log.error("Pooled thread " + this + " detected stale transaction " + current + " after performing " + this.runnable + " - will rollback and kill thread...");
            Queue<Throwable> stackTraces = current.getBeginTransactionStack();
            if(!CollectionUtils.isEmpty(stackTraces))
            {
                Throwable firstTxStack = stackTraces.poll();
                log.error("Stale transaction " + current + " started at: " + firstTxStack.getMessage(), firstTxStack);
                for(Throwable stackTrace = stackTraces.poll(); stackTrace != null; stackTrace = stackTraces.poll())
                {
                    log.error("Stale nested transaction " + current + " started at:" + stackTrace.getMessage(), stackTrace);
                }
            }
        }
        catch(Exception exc)
        {
            log.error("Unable to report a stale transaction found at " + this + "from its recorded stack", exc);
        }
    }


    void setBorrowed(boolean borrowed)
    {
        this.isBorrowed = borrowed;
    }


    public boolean isBorrowed()
    {
        return this.isBorrowed;
    }


    private final void changeProcessName()
    {
        if(this.runnable instanceof NamedProcess)
        {
            String n = ((NamedProcess)this.runnable).getProcessName();
            if(n != null)
            {
                this.myName = getName();
                setName(n);
            }
        }
    }


    private final RevertibleUpdate updateOperationInfo()
    {
        if(this.runnable instanceof ProcessWithOperationInfo)
        {
            return OperationInfo.updateThread(((ProcessWithOperationInfo)this.runnable).getOperationInfo());
        }
        return OperationInfo.updateThread(OperationInfo.builder().asNotSuspendableOperation().build());
    }


    private final void restoreProcessName()
    {
        if(this.myName != null)
        {
            setName(this.myName);
            this.myName = null;
        }
    }


    protected void handleError(Throwable e)
    {
        try
        {
            String msg = "Problems executing " + this.runnable + " : " + e.getMessage() + " \n" + Utilities.getStackTraceAsString(e);
            log.error(msg);
            if(this.runnable instanceof SelfLoggingProcess && ((SelfLoggingProcess)this.runnable).isErrorEnabled())
            {
                ((SelfLoggingProcess)this.runnable).error(msg);
            }
        }
        catch(Exception e2)
        {
            String msg = "Problems executing <error getting name>: " + e.getMessage() + " \n" + Utilities.getStackTraceAsString(e);
            log.error(msg);
            if(this.runnable instanceof SelfLoggingProcess && ((SelfLoggingProcess)this.runnable).isErrorEnabled())
            {
                ((SelfLoggingProcess)this.runnable).error(msg);
            }
        }
    }


    public void internalRun()
    {
        if(this.runnable == null)
        {
            log.error("Don't call run() call do() instead.");
            throw new IllegalStateException("Don't call run() call do() instead.");
        }
        this.isStarted = true;
        while(!this.isInvalid)
        {
            if(this.runnable == null)
            {
                break;
            }
            changeProcessName();
            RevertibleUpdate revertible = updateOperationInfo();
            try
            {
                this.pool.setTenant();
                this.runnable.run();
            }
            catch(Throwable e)
            {
                if(RedeployUtilities.isShutdownInProgress())
                {
                    clearInvalidTransactionIfNeeded();
                    revertible.revert();
                    restoreProcessName();
                    this.pool.unsetTenant();
                    try
                    {
                        resetAndReturnToPool();
                    }
                    catch(Exception exception)
                    {
                        log.error("Problems returning a thread to the ThreadPool: " + exception.getMessage() + " : " +
                                        Utilities.getStackTraceAsString(exception));
                        this.isInvalid = true;
                    }
                    break;
                }
                handleError(e);
            }
            finally
            {
                clearInvalidTransactionIfNeeded();
                revertible.revert();
                restoreProcessName();
                this.pool.unsetTenant();
                try
                {
                    resetAndReturnToPool();
                }
                catch(Exception e)
                {
                    log.error("Problems returning a thread to the ThreadPool: " + e.getMessage() + " : " + Utilities.getStackTraceAsString(e));
                    this.isInvalid = true;
                    break;
                }
            }
        }
    }


    private final void resetAndReturnToPool() throws Exception
    {
        synchronized(this)
        {
            this.runnable = null;
            this.pool.returnThread(this);
            if(!this.isInvalid)
            {
                try
                {
                    wait();
                }
                catch(InterruptedException interruptedException)
                {
                }
            }
        }
    }


    public void execute(Runnable rbl)
    {
        if(this.isInvalid)
        {
            throw new IllegalStateException("poolable thread is invalid - cannot execute");
        }
        if(!this.isBorrowed)
        {
            throw new IllegalStateException("poolable thread is not borrowed - cannot execute");
        }
        this.runnable = rbl;
        if(this.isStarted)
        {
            synchronized(this)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Notifying Thread: " + getName());
                }
                notify();
            }
        }
        else
        {
            if(log.isDebugEnabled())
            {
                log.debug("Starting Thread: " + getName());
            }
            super.start();
        }
    }


    public void invalidate()
    {
        if(this.isStarted)
        {
            synchronized(this)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Invalidating Thread: " + getName());
                }
                this.isInvalid = true;
                notify();
            }
        }
    }


    public Runnable getRunnable()
    {
        return this.runnable;
    }


    public void start()
    {
        throw new RuntimeException("please call execute on PoolableThread instead of start()");
    }


    boolean isValid()
    {
        return (!this.isStarted || (isAlive() && !this.isInvalid));
    }
}
