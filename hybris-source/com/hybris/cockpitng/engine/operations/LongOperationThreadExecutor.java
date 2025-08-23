/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

/**
 * Executes long operation in new thread.
 */
public interface LongOperationThreadExecutor
{
    /**
     * Executes given operation in a new thread.
     *
     * @param runnable
     *           operation to be executed.
     */
    default void execute(final Runnable runnable)
    {
        final Thread thread = new Thread(runnable);
        thread.start();
    }
}
