/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies.impl;

import de.hybris.platform.apiregistryservices.strategies.EventEmitStrategy;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock impl of @{@link EventEmitStrategy}
 */
public class MockEventEmitStrategy implements EventEmitStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(MockEventEmitStrategy.class);


    @Override
    public void sendEvent(final Object payload)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info(MessageFormat.format("Sending event : {0}", payload.getClass()));
        }
    }
}
