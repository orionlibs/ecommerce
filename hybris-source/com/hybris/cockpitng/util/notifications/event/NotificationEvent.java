/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.notifications.event;

/**
 * Communicates information across application.
 */
public class NotificationEvent extends com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent
{
    /**
     * @param source
     *           identity of notification source
     * @param eventType
     *           type of event that has taken place
     * @param level
     *           importance level of notification
     * @param referencedObjects
     */
    public NotificationEvent(final String source, final String eventType, final Level level, final Object... referencedObjects)
    {
        super(source, eventType, level, referencedObjects);
    }
}
