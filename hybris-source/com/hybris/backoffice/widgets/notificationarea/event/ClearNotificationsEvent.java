/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea.event;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

/**
 * Event used to clear displayed notifications.
 *
 * @deprecated since 1811
 * @see com.hybris.cockpitng.util.notifications.event.ClearNotificationsEvent
 */
@Deprecated(since = "1811", forRemoval = true)
public class ClearNotificationsEvent implements CockpitEvent
{
    public static final String EVENT_NAME = "ClearNotifications";
    private static final String NOT_APPLICABLE = "NA";
    private final NotificationEvent.Level level;
    private final String source;


    /**
     * @param source
     *           identity of notifications source
     * @param level
     *           importance level of notifications to be removed
     */
    public ClearNotificationsEvent(final String source, final NotificationEvent.Level level)
    {
        this.level = level;
        this.source = source;
    }


    /**
     * @param source
     *           identity of notification source
     */
    public ClearNotificationsEvent(final String source)
    {
        this(source, null);
    }


    @Override
    public String getName()
    {
        return EVENT_NAME;
    }


    @Override
    public Object getData()
    {
        return NOT_APPLICABLE;
    }


    @Override
    public String getSource()
    {
        return source;
    }


    public NotificationEvent.Level getLevel()
    {
        return level;
    }
}
