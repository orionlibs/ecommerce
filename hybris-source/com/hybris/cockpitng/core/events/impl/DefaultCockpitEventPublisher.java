/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.events.impl;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventPublisher;
import com.hybris.cockpitng.core.events.CockpitEventQueueConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standard implementation of the event publisher interface that uses {@link CockpitEventQueueConnector} to publish
 * events.
 */
public class DefaultCockpitEventPublisher implements CockpitEventPublisher
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitEventPublisher.class);
    private CockpitEventQueueConnector queueConnector;


    @Override
    public void publishEvent(final CockpitEvent event)
    {
        if(queueConnector == null)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn(String.format("No eventQueueConnector found, ignoring event %s", event));
            }
        }
        else
        {
            queueConnector.publishEvent(event);
        }
    }


    @Override
    public void setQueueConnector(final CockpitEventQueueConnector queueConnector)
    {
        this.queueConnector = queueConnector;
    }
}
