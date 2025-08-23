/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Utility methods for handling global cockpit events.
 */
public final class CockpitEventUtils
{
    public static final String GLOBAL_EVENT_LISTENERS_MAP = "globalEventListenersMap";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitEventUtils.class);
    private static final String DISABLE_GLOBAL_EVENTS = "disableGlobalEvents";


    private CockpitEventUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Disable/enable global events
     *
     * @param disabled
     */
    public static void disableGlobalEvents(final boolean disabled)
    {
        final Execution execution = Executions.getCurrent();
        if(execution != null && execution.getDesktop() != null)
        {
            execution.getDesktop().setAttribute("disableGlobalEvents", disabled);
        }
    }


    private static Map<String, Map<String, EventListener<Event>>> getGlobalListenersFromDesktop()
    {
        final Execution execution = Executions.getCurrent();
        if(execution == null)
        {
            return null;
        }
        try
        {
            return (Map<String, Map<String, EventListener<Event>>>)execution.getDesktop().getAttribute(GLOBAL_EVENT_LISTENERS_MAP);
        }
        catch(final ClassCastException e)
        {
            LOG.error("Wrong type for attribute '" + GLOBAL_EVENT_LISTENERS_MAP + "'", e);
            return null;
        }
    }


    /**
     * Fetches all events that are currently in the queue for the current desktop and calls them. For internal use only.
     */
    public static void dispatchGlobalEvents(final CockpitEventQueue cockpitEventQueue)
    {
        if(Executions.getCurrent() != null && Executions.getCurrent().getDesktop() != null)
        {
            final Boolean disabled = (Boolean)Executions.getCurrent().getDesktop().getAttribute(DISABLE_GLOBAL_EVENTS);
            if(BooleanUtils.isNotTrue(disabled))
            {
                final Map<String, Map<String, EventListener<Event>>> globalListenersFromDesktop = getGlobalListenersFromDesktop();
                if(globalListenersFromDesktop != null)
                {
                    synchronized(globalListenersFromDesktop)
                    {
                        for(final Entry<String, Map<String, EventListener<Event>>> listenerEntry : globalListenersFromDesktop
                                        .entrySet())
                        {
                            final String widgetslotUuid = listenerEntry.getKey();
                            final List<CockpitEvent> fetchEvents = cockpitEventQueue.fetchEvents(widgetslotUuid);
                            for(final CockpitEvent cockpitEvent : fetchEvents)
                            {
                                final String eventName = cockpitEvent.getName();
                                final Map<String, EventListener<Event>> listeners4Widget = listenerEntry.getValue();
                                if(listeners4Widget != null && eventName != null)
                                {
                                    final EventListener<Event> eventListener = listeners4Widget.get(eventName);
                                    if(eventListener != null)
                                    {
                                        try
                                        {
                                            eventListener.onEvent(new Event(cockpitEvent.getName(), null, cockpitEvent));
                                        }
                                        catch(final Exception e)
                                        {
                                            LOG.error("Error while executing event listener: ", e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
