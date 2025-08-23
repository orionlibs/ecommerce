/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;

/**
 * Default implementation of <b>LazyTaskExecutorFactory</b>
 */
public class DefaultLazyTasksExecutorFactory implements LazyTasksExecutorFactory
{
    protected static final String LAZY_TASK_EXECUTOR = "lazyTaskExecutor";


    /**
     * Factory method. The main purpose is to get spring bean and register it into model
     *
     * @param wim WidgetInstanceManager of widget. Lazy tasks are executed for this widget
     * @return DefaultLazyTasksExecutor instance
     */
    @Override
    public DefaultLazyTasksExecutor getInstance(final WidgetInstanceManager wim)
    {
        DefaultLazyTasksExecutor executor = wim.getModel().getValue(LAZY_TASK_EXECUTOR, DefaultLazyTasksExecutor.class);
        if(executor == null || !StringUtils.equals(executor.getIdentifier(), wim.getWidgetslot().getUuid()))
        {
            executor = getLazyTasksExecutor();
            executor.setWidgetInstanceManager(wim);
            executor.setCleanUpCallback(removeInstanceFromModel(wim));
            executor.setIdentifier(wim.getWidgetslot().getUuid());
            wim.getModel().setValue(LAZY_TASK_EXECUTOR, executor);
            executor.autoExecuteInNextUiCall();
        }
        return executor;
    }


    protected DefaultLazyTasksExecutor getLazyTasksExecutor()
    {
        return (DefaultLazyTasksExecutor)SpringUtil.getBean("lazyTasksExecutor", DefaultLazyTasksExecutor.class);
    }


    protected Executable removeInstanceFromModel(final WidgetInstanceManager wim)
    {
        return () ->
                        wim.getModel().setValue(LAZY_TASK_EXECUTOR, null);
    }
}
