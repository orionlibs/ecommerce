/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.DesktopCtrl;

/**
 * Backoffice background operation that is capable of performing long running tasks in background allowing the UI to
 * remain responsive.<br>
 * After long running task is completed an operation to update<br>
 * the UI is scheduled via {@link Executions#schedule(Desktop, EventListener, Event)} method.<br>
 * <br>
 * The contract for background task is to execute normally and return the result or throw {@link RuntimeException} in
 * case of error. <br>
 * <br>
 * Background task should not interact with ZK UI neither via {@link Executions#schedule(Desktop, EventListener, Event)}
 * nor via {@link Executions#activate(Desktop)}
 */
public class CockpitNGBackgroundOperation implements Runnable, Cancellable
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitNGBackgroundOperation.class);
    private final WeakReference<Desktop> desktopReference;
    private final RequestOperationContextHolder.ContextRequest contextRequest;
    private final Supplier backgroundTask;
    private final Consumer<Object> onSuccess;
    private final Consumer<Exception> onError;
    private final UUID taskId = UUID.randomUUID();
    private final AtomicBoolean cancelled = new AtomicBoolean();
    private final StopWatch stopWatch = new StopWatch();


    /**
     * Creates new instance of {@link CockpitNGBackgroundOperation}
     *
     * @param contextRequest
     *           context of currently running server request
     * @param backgroundTask
     *           long running task to be executed
     * @param onSuccess
     *           callback to be executed after background tasks finishes successfully.<br>
     *           This callback will be scheduled for the UI thread via
     *           {@link Executions#schedule(Desktop, EventListener, Event)}
     * @param onError
     *           callback to be executed on background task error.<br>
     *           This callback will be scheduled for the UI thread via
     *           {@link Executions#schedule(Desktop, EventListener, Event)}
     */
    public CockpitNGBackgroundOperation(final Desktop desktop, final RequestOperationContextHolder.ContextRequest contextRequest,
                    final Supplier backgroundTask, final Consumer<Object> onSuccess, final Consumer<Exception> onError)
    {
        this.desktopReference = new WeakReference<>(desktop);
        this.contextRequest = contextRequest;
        this.backgroundTask = backgroundTask;
        this.onSuccess = onSuccess;
        this.onError = onError;
        ((DesktopCtrl)desktop).enableServerPush(true, taskId);
        LOG.debug("Initializing background operation: {}, task={}; server push enabled", desktopReference.get(), taskId);
    }


    @Override
    public void run()
    {
        final Desktop desktop = this.desktopReference.get();
        if(desktop == null || !desktop.isAlive())
        {
            return;
        }
        RequestOperationContextHolder.instance().executeWithContext(() -> {
            try
            {
                runInternal(desktop);
            }
            catch(final DesktopUnavailableException e)
            {
                LOG.warn("Background operation failed: taskId=" + taskId, e);
            }
            catch(final Exception e)
            {
                if(onError != null)
                {
                    scheduleForUI(e, onError);
                }
            }
        }, contextRequest);
    }


    @Override
    public void cancel()
    {
        LOG.debug("Canceling background operation: taskId={}", taskId);
        cancelled.set(true);
    }


    protected void runInternal(final Desktop desktop)
    {
        Object result = null;
        if(!cancelled.get())
        {
            LOG.debug("Background operation started: {}, task={}", desktop, taskId);
            stopWatch.start();
            result = backgroundTask.get();
            stopWatch.stop();
            LOG.debug("Background operation completed in {} : {}, task={}", stopWatch, desktop, taskId);
        }
        if(onSuccess != null)
        {
            scheduleForUI(result, onSuccess);
        }
    }


    protected void scheduleForUI(final Object result, final Consumer consumer)
    {
        final Desktop desktop = desktopReference.get();
        if(desktop != null && desktop.isAlive())
        {
            LOG.debug("Scheduling UI update: {}, taskId={}", desktop, taskId);
            stopWatch.reset();
            stopWatch.start();
            Executions.schedule(desktop, event -> {
                stopWatch.stop();
                LOG.debug("Updating UI after {}: {}, taskId={}", stopWatch, desktop, taskId);
                stopWatch.reset();
                stopWatch.start();
                consumer.accept(event.getData());
                ((DesktopCtrl)desktop).enableServerPush(false, taskId);
                stopWatch.stop();
                LOG.debug("UI updated in {}: {}, taskId={}; server push disabled", stopWatch, desktop, taskId);
            }, new Event("onBackgroundOperationFinished", null, result));
        }
        else
        {
            LOG.debug("Desktop is not available UI cannot be updated: {}, taskId={}", desktop, taskId);
        }
    }


    /**
     * @deprecated since 2105, no longer used
     */
    @Deprecated(since = "2105", forRemoval = true)
    void attachRequestContext()
    {
        RequestOperationContextHolder.instance().attachToContext(contextRequest);
    }
}
