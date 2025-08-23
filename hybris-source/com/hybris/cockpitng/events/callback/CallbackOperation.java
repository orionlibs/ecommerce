/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events.callback;

import com.hybris.cockpitng.events.callback.impl.CallbackChain;

/**
 * Callback for operations. Usually used for conversations with the user that require asynchronous operations.
 */
public interface CallbackOperation<T>
{
    /**
     * The operation to be called via chain
     * @param chain chain of callbacks to be invoked; this method should be called manually by each listener
     * @param context context
     * @param value value to be passed to the chain
     */
    void invoke(final CallbackChain chain, T context, Object value);
}
