/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.util.impl;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCockpitGlobalEventPublisher implements CockpitGlobalEventPublisher
{
    private CockpitProperties cockpitProperties;
    private CockpitEventQueue eventQueue;


    @Override
    public void publish(final Object eventSource, final String eventName, final Object eventData, final Context eventContext)
    {
        if(eventQueue != null && isCockpitEventNotificationEnabled() && isCockpitEventNotificationEnabled(eventContext))
        {
            final CockpitEvent event = createEvent(eventSource, eventName, eventData);
            populateEventContext(eventContext, event);
            eventQueue.publishEvent(event);
        }
    }


    @Override
    public void publish(final String eventName, final Object eventData, final Context eventContext)
    {
        publish(null, eventName, eventData, eventContext);
    }


    protected CockpitEvent createEvent(final Object eventSource, final String eventName, final Object eventData)
    {
        return new DefaultCockpitEvent(eventName, eventData, eventSource);
    }


    protected boolean isCockpitEventNotificationEnabled()
    {
        return BooleanUtils
                        .isNotFalse(BooleanUtils.toBooleanObject(cockpitProperties.getProperty(SETTING_COCKPIT_EVENT_NOTIFICATION)));
    }


    protected boolean isCockpitEventNotificationEnabled(final Context ctx)
    {
        return ctx == null || BooleanUtils.isNotFalse((Boolean)ctx.getAttribute(CTX_COCKPIT_EVENT_NOTIFICATION));
    }


    protected void populateEventContext(final Context source, final CockpitEvent destination)
    {
        if(source != null && DefaultCockpitEvent.class.isAssignableFrom(destination.getClass()))
        {
            source.getAttributeNames().forEach(attributeName -> ((DefaultCockpitEvent)destination).getContext().put(attributeName,
                            source.getAttribute(attributeName)));
        }
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected CockpitEventQueue getEventQueue()
    {
        return eventQueue;
    }


    @Required
    public void setEventQueue(final CockpitEventQueue eventQueue)
    {
        this.eventQueue = eventQueue;
    }
}
