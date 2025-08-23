/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Factory for <b>LazyTasksExecutor</b>
 */
public interface LazyTasksExecutorFactory
{
    LazyTasksExecutor getInstance(WidgetInstanceManager wim);
}
