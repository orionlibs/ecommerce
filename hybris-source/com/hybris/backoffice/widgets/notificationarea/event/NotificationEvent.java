/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea.event;

import com.hybris.cockpitng.core.events.CockpitEvent;
import java.util.Arrays;

/**
 * Communicates information across application.
 *
 * @deprecated since 1811
 * @see com.hybris.cockpitng.util.notifications.event.NotificationEvent
 */
@Deprecated(since = "1811", forRemoval = true)
public class NotificationEvent implements CockpitEvent
{
    public static final String EVENT_SOURCE_UNKNOWN = "unknown";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String EVENT_TYPE_UNKNOWN = "unknown";
    public static final String EVENT_NAME = "Notification";
    private final String source;
    private final String eventType;
    private final Level level;
    private final Object[] referencedObjects;


    /**
     * @param source
     *           identity of notification source
     * @param eventType
     *           type of event that has taken place
     * @param level
     *           importance level of notification
     * @param referencedObjects
     *           optional array of objects that were involved in the event
     */
    public NotificationEvent(final String source, final String eventType, final Level level, final Object... referencedObjects)
    {
        this.source = source;
        this.eventType = eventType;
        this.level = level;
        this.referencedObjects = referencedObjects;
    }


    /**
     * @return importance level of notification
     */
    public Level getLevel()
    {
        return level;
    }


    @Override
    public String getName()
    {
        return EVENT_NAME;
    }


    @Override
    public Object getData()
    {
        return this.eventType;
    }


    @Override
    public String getSource()
    {
        return source;
    }


    /**
     * @return type of event that has taken place
     */
    public String getEventType()
    {
        return eventType;
    }


    public Object[] getReferencedObjects()
    {
        return referencedObjects;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final NotificationEvent that = (NotificationEvent)o;
        if(source != null ? !source.equals(that.source) : (that.source != null))
        {
            return false;
        }
        if(eventType != null ? !eventType.equals(that.eventType) : (that.eventType != null))
        {
            return false;
        }
        if(level != that.level)
        {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(referencedObjects, that.referencedObjects);
    }


    @Override
    public int hashCode()
    {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(referencedObjects);
        return result;
    }


    /**
     * Indicates severity level of the message.
     */
    public enum Level
    {
        SUCCESS, INFO, WARNING, FAILURE
    }
}
