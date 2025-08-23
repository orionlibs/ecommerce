/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

import com.hybris.cockpitng.util.CockpitThreadContextCreator;

/**
 * Executes operation with a result in background thread.
 *
 * @param <RESULT> type of the result.
 */
public abstract class ResultLongOperation<RESULT> extends LongOperation
{
    private RESULT result;


    /**
     * @param cockpitThreadContextCreator util used to copy ctx from ui thread to background thread.
     */
    public ResultLongOperation(final CockpitThreadContextCreator cockpitThreadContextCreator)
    {
        super(cockpitThreadContextCreator);
    }


    /**
     * Background operation which returns a result.
     *
     * @return result of background operation.
     * @throws InterruptedException
     */
    protected abstract RESULT getResult() throws InterruptedException;


    /**
     * Foreground operation which is called with result from background operation {@link #getResult()}
     *
     * @param result result of the operation.
     */
    protected abstract void onResult(RESULT result);


    @Override
    protected void execute() throws InterruptedException
    {
        result = getResult();
    }


    @Override
    protected final void onFinish()
    {
        onResult(result);
    }
}
