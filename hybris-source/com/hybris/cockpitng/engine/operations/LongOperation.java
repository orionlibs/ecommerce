/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import com.hybris.cockpitng.util.CockpitThreadContextCreator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.sys.DesktopCache;
import org.zkoss.zk.ui.sys.DesktopCtrl;
import org.zkoss.zk.ui.sys.WebAppCtrl;

/**
 * Executes operation in background thread.
 */
public abstract class LongOperation implements Runnable, Cancellable
{
    private static final Logger LOG = LoggerFactory.getLogger(LongOperation.class);
    private static final int LONG_OP_ACTIVATION_TIMEOUT = 60000;
    private String desktopId;
    private DesktopCache desktopCache;
    private RequestOperationContextHolder.ContextRequest contextRequest;
    private Thread thread;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean desktopActivated = new AtomicBoolean(false);
    private final CockpitThreadContextCreator cockpitThreadContextCreator;
    private final UUID taskId = UUID.randomUUID();


    /**
     * @param cockpitThreadContextCreator
     *           util used to copy ctx from ui thread to background thread.
     */
    public LongOperation(final CockpitThreadContextCreator cockpitThreadContextCreator)
    {
        this.cockpitThreadContextCreator = cockpitThreadContextCreator;
    }


    /**
     * asynchronous callback for your long operation code
     *
     * @throws InterruptedException
     *            if operation has been Interrupted
     */
    protected abstract void execute() throws InterruptedException;


    /**
     * Callback method which is executed in Ui thread after long operation finishes.
     */
    protected void onFinish()
    {
    }


    /**
     * optional callback method when the task has been cancelled or was interrupted otherwise
     */
    protected void onCancel()
    {
    }


    /**
     * optional callback method when the task has completed with an uncaught Exception
     *
     * @param exception
     *           thrown exception
     */
    protected void onException(final Throwable exception)
    {
        LOG.error("Long operation error", exception);
    }


    /**
     * Optional callback method when the task has completed (always called)
     */
    protected void onCleanup()
    {
    }


    /**
     * set the cancelled flag and try to interrupt the thread
     */
    public final void cancel()
    {
        cancelled.set(true);
        if(thread != null)
        {
            thread.interrupt();
        }
    }


    /**
     * check the cancelled flag
     *
     * @return true if operation has been cancelled.
     */
    public final boolean isCancelled()
    {
        return cancelled.get();
    }


    /**
     * activate the thread (and cached desktop) for UI updates call {@link #deactivate()} once done updating the UI
     *
     * @return true if desktop is activated.
     * @throws InterruptedException
     *            if it is interrupted by other thread
     */
    protected final boolean activate() throws InterruptedException
    {
        if(!desktopActivated.get())
        {
            if(Executions.activate(getDesktop(), LONG_OP_ACTIVATION_TIMEOUT))
            {
                desktopActivated.set(true);
            }
            else
            {
                LOG.warn("Cannot activate desktop for long operation. Ui will not be updated");
            }
        }
        return desktopActivated.get();
    }


    /**
     * deactivate the current active (see: {@link #activate()}) thread/desktop after updates are done
     */
    protected final void deactivate()
    {
        if(desktopActivated.get())
        {
            Executions.deactivate(getDesktop());
            desktopActivated.set(false);
        }
    }


    /**
     * Checks if the task thread has been interrupted. Use this to check whether or not to exit a busy operation in case.
     *
     * @throws InterruptedException
     *            when the current task has been cancelled/interrupted
     */
    protected final void checkCancelled() throws InterruptedException
    {
        if(this.thread == null)
        {
            return;
        }
        if(Thread.currentThread() != this.thread)
        {
            throw new IllegalStateException("this method can only be called in the worker thread (i.e. during execute)");
        }
        final boolean interrupted = Thread.interrupted();
        if(interrupted || cancelled.get())
        {
            cancelled.set(true);
            throw new InterruptedException();
        }
    }


    /**
     * launch the long operation
     */
    public final void start()
    {
        // not caching the desktop directly to enable garbage collection, in case the desktop destroyed during the long
        // operation
        this.desktopId = Executions.getCurrent().getDesktop().getId();
        this.desktopCache = ((WebAppCtrl)WebApps.getCurrent()).getDesktopCache(Sessions.getCurrent());
        enableServerPushForThisTask();
        this.contextRequest = RequestOperationContextHolder.instance().prepareForOperation();
        cockpitThreadContextCreator.execute(this);
    }


    @Override
    public final void run()
    {
        try
        {
            thread = Thread.currentThread();
            RequestOperationContextHolder.instance().executeWithContext(() -> {
                runInternal();
            }, contextRequest);
        }
        finally
        {
            try
            {
                //always deactivate no matter what
                Executions.deactivate(getDesktop());
                disableServerPushForThisTask();
            }
            catch(final ComponentNotFoundException cfe)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Error occurred ", cfe);
                }
                // not relevant at this point
            }
        }
    }


    /**
     * Activates desktop before execution of executable and deactivates after executable finishes.
     *
     * @param executable
     *           operation to execute.
     * @throws InterruptedException
     *            when operation is interrupted
     */
    private void executeInUiThread(final Executable executable) throws InterruptedException
    {
        if(activate())
        {
            try
            {
                executable.execute();
            }
            finally
            {
                deactivate();
            }
        }
    }


    private static void logCannotFinishOperationAfterReload()
    {
        LOG.warn("Cannot finish background operation, probably page was reloaded during operation");
    }


    private void onFinishInternal()
    {
        try
        {
            onFinish();
            onCleanup();
        }
        catch(final Exception ex)
        {
            onException(ex);
        }
    }


    private void enableServerPushForThisTask()
    {
        ((DesktopCtrl)getDesktop()).enableServerPush(true, taskId);
    }


    private void disableServerPushForThisTask()
    {
        ((DesktopCtrl)getDesktop()).enableServerPush(false, taskId);
    }


    private Desktop getDesktop()
    {
        return desktopCache.getDesktop(desktopId);
    }


    private void runInternal()
    {
        try
        {
            checkCancelled();
            execute();
            checkCancelled();
            executeInUiThread(this::onFinishInternal);
        }
        catch(final InterruptedException e)
        {
            try
            {
                cancelled.set(true);
                executeInUiThread(() -> {
                    onCancel();
                    onCleanup();
                });
            }
            catch(final InterruptedException ie)
            {
                Thread.currentThread().interrupt();
                throw new LongOperationException("interrupted onCancel handling", ie);
            }
            catch(final ComponentNotFoundException | DesktopUnavailableException ex)
            {
                logCannotFinishOperationAfterReload();
            }
            Thread.currentThread().interrupt();
        }
        catch(final ComponentNotFoundException | DesktopUnavailableException ex)
        {
            logCannotFinishOperationAfterReload();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred on long operation ", ex);
            }
        }
        catch(final Exception ex)
        {
            defaultExceptionHandle(ex);
        }
    }


    private void defaultExceptionHandle(final Exception ex)
    {
        try
        {
            executeInUiThread(() -> {
                onException(ex);
                onCleanup();
            });
        }
        catch(final InterruptedException ie)
        {
            Thread.currentThread().interrupt();
            throw new LongOperationException("interrupted onException handling", ie);
        }
        catch(final ComponentNotFoundException | DesktopUnavailableException e)
        {
            logCannotFinishOperationAfterReload();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred on long operation error handling ", e);
            }
        }
    }
}
