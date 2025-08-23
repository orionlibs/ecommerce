/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.operations.LongOperation;
import com.hybris.cockpitng.engine.operations.ResultLongOperation;
import com.hybris.cockpitng.util.CockpitComponentCleanup;
import com.hybris.cockpitng.util.CockpitThreadContextCreator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

/**
 * Default implementation of <b>LazyTasksExecutor</b>.
 * Provides functionality for managing and executing lazy tasks
 */
public class DefaultLazyTasksExecutor implements LazyTasksExecutor
{
    protected static final String ON_LAZY_LOAD_TASK_STARTED = "onLazyLoadTaskStarted";
    private WidgetInstanceManager widgetInstanceManager;
    private final List<LazyTask> taskList;
    private ResultLongOperation<List<LazyTask>> longOperation;
    private Executable cleanUpCallback;
    private CockpitThreadContextCreator cockpitThreadContextCreator;
    private String identifier;


    public DefaultLazyTasksExecutor()
    {
        taskList = new ArrayList<>();
    }


    @Override
    public void scheduleTask(final LazyTask lazyTask)
    {
        taskList.add(lazyTask);
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public void autoExecuteInNextUiCall()
    {
        final Cleanable cleanable = () ->
        {
            onCleanupLazyTaskExecutor();
            if(longOperation != null)
            {
                longOperation.cancel();
            }
        };
        final EventListener<Event> echoEventListener = new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event echoEvent) throws Exception
            {
                onCleanupLazyTaskExecutor();
                getWidgetInstanceManager().getWidgetslot().removeEventListener(ON_LAZY_LOAD_TASK_STARTED, this);
                unregisterCleanable(cleanable);
                executeOperation((List<LazyTask>)echoEvent.getData());
            }
        };
        getWidgetInstanceManager().getWidgetslot().addEventListener(ON_LAZY_LOAD_TASK_STARTED, echoEventListener);
        registerCleanable(cleanable);
        Events.echoEvent(ON_LAZY_LOAD_TASK_STARTED, getWidgetInstanceManager().getWidgetslot(), taskList);
    }


    protected void registerCleanable(final Cleanable cleanable)
    {
        CockpitComponentCleanup.registerCleanable(getWidgetInstanceManager().getWidgetslot(), cleanable);
    }


    protected void unregisterCleanable(final Cleanable cleanable)
    {
        CockpitComponentCleanup.unregisterCleanable(getWidgetInstanceManager().getWidgetslot(), cleanable);
    }


    protected void onCleanupLazyTaskExecutor()
    {
        if(cleanUpCallback != null)
        {
            cleanUpCallback.execute();
        }
    }


    protected void executeOperation(final List<LazyTask> taskList)
    {
        longOperation = new ResultLongOperation<List<LazyTask>>(cockpitThreadContextCreator)
        {
            @Override
            protected List<LazyTask> getResult()
            {
                taskList.forEach(LazyTask::executeTask);
                return taskList;
            }


            @Override
            protected void onResult(final List<LazyTask> result)
            {
                result.forEach(LazyTask::executeOnDone);
            }


            @Override
            protected void onException(final Throwable exception)
            {
                super.onException(exception);
                Messagebox.show(exception.getLocalizedMessage(), null, Messagebox.OK, Messagebox.ERROR, 0, null);
            }
        };
        startLongOperation(longOperation);
    }


    protected void startLongOperation(final LongOperation operation)
    {
        operation.start();
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        if(widgetInstanceManager == null)
        {
            throw new IllegalStateException("WidgetInstanceManager is required");
        }
        return widgetInstanceManager;
    }


    public List<LazyTask> getTaskList()
    {
        return taskList;
    }


    public void setCleanUpCallback(final Executable cleanUpCallback)
    {
        this.cleanUpCallback = cleanUpCallback;
    }


    @Required
    public void setCockpitThreadContextCreator(final CockpitThreadContextCreator cockpitThreadContextCreator)
    {
        this.cockpitThreadContextCreator = cockpitThreadContextCreator;
    }


    public void setIdentifier(final String identifier)
    {
        this.identifier = identifier;
    }
}
