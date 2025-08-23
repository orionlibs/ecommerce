/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.notifications;

import com.hybris.cockpitng.util.notifications.event.ClearNotificationsEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

/**
 * Service responsible for sending user notifications. It uses {@link NotificationEvent} to publish notifications and
 * {@link ClearNotificationsEvent} to clear them.
 */
public class DefaultNotificationService extends com.hybris.backoffice.widgets.notificationarea.DefaultNotificationService implements NotificationService
{
    //NOOP
}
