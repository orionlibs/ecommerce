/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Timer;

public class EventAcceptorWidgetController extends DefaultWidgetController
{
    protected static final String SOCKET_OUT_GENERIC_OUTPUT = "genericOutput";
    protected static final String SETTING_POLLING_DELAY = "pollingDelay";
    protected static final String SETTING_EVENT_SCOPE = "eventScope";
    protected static final String SETTING_EVENT_NAME = "eventName";
    private static final Logger LOG = LoggerFactory.getLogger(EventAcceptorWidgetController.class);
    private static final long serialVersionUID = 1L;
    private transient CockpitEventQueue cockpitEventQueue;


    @Override
    public void initialize(final Component comp)
    {
        final String eventName = getWidgetSettings().getString(SETTING_EVENT_NAME);
        final String eventScope = getWidgetSettings().getString(SETTING_EVENT_SCOPE);
        if(StringUtils.isNotBlank(eventName))
        {
            cockpitEventQueue.registerListener(getWidgetslot().getUuid(), eventName, eventScope);
            final Timer timer = createTimer();
            timer.setDelay(getTimerDelay());
            timer.setRepeats(true);
            timer.setRunning(true);
            timer.addEventListener(Events.ON_TIMER, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    final List<CockpitEvent> dispatchEvents = cockpitEventQueue.fetchEvents(getWidgetslot().getUuid());
                    for(final CockpitEvent cockpitEvent : dispatchEvents)
                    {
                        sendOutput(SOCKET_OUT_GENERIC_OUTPUT, cockpitEvent.getData());
                    }
                }
            });
            comp.appendChild(timer);
        }
        else
        {
            LOG.error("Setting '{}' is not set", SETTING_EVENT_NAME);
        }
    }


    protected void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    protected Timer createTimer()
    {
        return new Timer();
    }


    private int getTimerDelay()
    {
        final int delay = getWidgetSettings().getInt(SETTING_POLLING_DELAY);
        final int defaultDelay = 500;
        return delay > 0 ? delay : defaultDelay;
    }
}
