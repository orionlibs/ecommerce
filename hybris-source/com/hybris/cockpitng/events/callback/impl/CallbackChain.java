/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.callback.impl;

/**
 * Interface for callback chains.
 *
 * @see com.hybris.cockpitng.events.callback.CallbackOperation
 */
public interface CallbackChain<T>
{
    /**
     * Method used to invoke the next operation in the chain.
     *
     * @param context context
     * @param value value to be passed to the chain
     */
    void doChain(T context, Object value);
}
