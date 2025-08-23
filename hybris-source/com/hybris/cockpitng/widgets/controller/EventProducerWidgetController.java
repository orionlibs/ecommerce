/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Emits events with the supplied data.
 */
public class EventProducerWidgetController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_GENERIC_INPUT = "genericInput";
    protected static final String SETTING_EVENT_NAME = "eventName";
    protected static final String EVENT_INITIAL_VALUE = "default";
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(EventProducerWidgetController.class);
    private transient CockpitEventQueue cockpitEventQueue;


    @SocketEvent(socketId = SOCKET_IN_GENERIC_INPUT)
    public void inputReceived(final Object data)
    {
        final String eventName = getWidgetSettings().getString(SETTING_EVENT_NAME);
        if(StringUtils.isNotBlank(eventName))
        {
            cockpitEventQueue.publishEvent(new DefaultCockpitEvent(eventName, data, this));
        }
        else
        {
            LOG.error("Setting '{}' is not defined", SETTING_EVENT_NAME);
        }
    }


    /*
     * For unit tests.
     */
    protected void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }
}
