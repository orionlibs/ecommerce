/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.notifications.event;

/**
 * Event used to clear displayed notifications.
 */
public class ClearNotificationsEvent extends com.hybris.backoffice.widgets.notificationarea.event.ClearNotificationsEvent
{
    public ClearNotificationsEvent(final String source, final NotificationEvent.Level level)
    {
        super(source, level);
    }


    public ClearNotificationsEvent(final String source)
    {
        super(source);
    }
}
