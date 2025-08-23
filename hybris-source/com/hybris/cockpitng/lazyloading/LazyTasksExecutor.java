/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

/**
 * Manages scheduling and executing lazy tasks
 */
public interface LazyTasksExecutor
{
    void scheduleTask(final LazyTask lazyTask);
}
