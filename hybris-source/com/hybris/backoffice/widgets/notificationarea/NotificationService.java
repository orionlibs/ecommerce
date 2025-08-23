/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;

/**
 * Notification service responsible for sending user notifications.
 *
 * @deprecated since 1811, use {@link com.hybris.cockpitng.util.notifications.NotificationService} instead
 * @see com.hybris.cockpitng.util.notifications.NotificationService
 */
@Deprecated(since = "1811", forRemoval = true)
public interface NotificationService
{
    /**
     * Sends a notification to end user.
     *
     * @param source
     *           identity of notification source.
     * @param eventType
     *           type of notification to be sent {@link NotificationEventTypes}.
     * @param level
     *           importance level of notification.
     * @param referenceObjects
     *           optional array of objects that are related to event.
     */
    void notifyUser(String source, String eventType, NotificationEvent.Level level, Object... referenceObjects);


    /**
     * Sends clear event which will remove all notifications from specified source.
     *
     * @param source
     *           identity of notification source.
     */
    void clearNotifications(String source);


    /**
     * Sends clear event which will remove all notifications from specified source of specified importance level.
     *
     * @param source
     *           identity of notification source.
     * @param level
     *           importance level of notifications to be removed.
     */
    void clearNotifications(String source, NotificationEvent.Level level);


    /**
     * Gets default source for each notification sent by specified widget.
     *
     * @param widgetInstanceManager
     *           instance manager of a widget which sends notification.
     * @return source name for notification.
     */
    String getWidgetNotificationSource(WidgetInstanceManager widgetInstanceManager);


    /**
     * Sends a notification to end user.
     *
     * @param widgetInstanceManager
     *           instance manager of a widget which sends notification.
     * @param eventType
     *           type of notification to be sent {@link NotificationEventTypes}.
     * @param level
     *           importance level of notification.
     * @param referenceObjects
     *           optional array of objects that are related to event.
     * @see #getWidgetNotificationSource(WidgetInstanceManager)
     */
    default void notifyUser(final WidgetInstanceManager widgetInstanceManager, final String eventType,
                    final NotificationEvent.Level level, final Object... referenceObjects)
    {
        notifyUser(getWidgetNotificationSource(widgetInstanceManager), eventType, level, referenceObjects);
    }


    /**
     * Gets default source for each notification sent by specified action.
     *
     * @param context
     *           action context.
     * @return source name for notification.
     */
    String getWidgetNotificationSource(ActionContext<?> context);


    /**
     * Sends a notification to end user.
     *
     * @param context
     *           action context.
     * @param eventType
     *           type of notification to be sent {@link NotificationEventTypes}.
     * @param level
     *           importance level of notification.
     * @param referenceObjects
     *           optional array of objects that are related to event.
     * @see #getWidgetNotificationSource(ActionContext)
     */
    default void notifyUser(final ActionContext<?> context, final String eventType, final NotificationEvent.Level level,
                    final Object... referenceObjects)
    {
        notifyUser(getWidgetNotificationSource(context), eventType, level, referenceObjects);
    }
}
