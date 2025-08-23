/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which collects lazy task, its result and consumer of task result
 * Methods of this class must be called in special order:
 * 1. executeTask
 * 2. executeOnDone
 * <p>
 * If the order is reversed, an exception will be thrown.
 *
 * @param <LAZY_DATA> type of lazy task's result
 */
public class DefaultLazyTask<LAZY_DATA> implements LazyTask
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLazyTask.class);
    private final Supplier<LAZY_DATA> dataSupplier;
    private final Consumer<LazyTaskResult<LAZY_DATA>> dataConsumer;
    private LazyTaskResult<LAZY_DATA> result;
    private boolean taskExecuted = false;


    public DefaultLazyTask(final Supplier<LAZY_DATA> dataSupplier, final Consumer<LazyTaskResult<LAZY_DATA>> dataConsumer)
    {
        this.dataSupplier = dataSupplier;
        this.dataConsumer = dataConsumer;
    }


    @Override
    public void executeTask()
    {
        try
        {
            taskExecuted = true;
            result = DefaultLazyTaskResult.success(dataSupplier.get());
        }
        catch(final RuntimeException e)
        {
            LOG.error("Unable to execute task", e);
            result = DefaultLazyTaskResult.failure();
        }
    }


    @Override
    public void executeOnDone()
    {
        if(!taskExecuted)
        {
            throw new IllegalStateException("Unexpected method call. Did you forget to call 'executeTask' method?");
        }
        dataConsumer.accept(result);
    }
}
