/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.au;

import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import org.springframework.core.Ordered;
import org.zkoss.zk.au.AuResponse;

/**
 * Abstract class capable of processing {@link AuResponse}.
 */
public abstract class AuResponseProcessor<R extends AuResponse> implements Ordered
{
    protected Class<? extends R> getSupportedResponseClass()
    {
        return ReflectionUtils.extractGenericParameterType(getClass(), AuResponseProcessor.class);
    }


    /**
     * Checks if specified response may be processed by this processor.
     *
     * @param response
     *           response to be processed
     * @return <code>true</code> if it meets processor's expectations
     */
    public boolean canHandle(final AuResponse response)
    {
        return getSupportedResponseClass().isInstance(response);
    }


    /**
     * Processes specified {@link AuResponse}.
     *
     * @param response
     *           response to be processed
     */
    public abstract void process(final R response);


    @Override
    public int getOrder()
    {
        return 0;
    }
}
