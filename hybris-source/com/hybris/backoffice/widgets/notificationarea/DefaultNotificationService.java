/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea;

import com.hybris.backoffice.widgets.notificationarea.event.ClearNotificationsEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.UiException;

/**
 * Service responsible for sending user notifications. It uses {@link NotificationEvent} to publish notifications and
 * {@link ClearNotificationsEvent} to clear them.
 *
 * @deprecated since 1811
 * @see com.hybris.cockpitng.util.notifications.DefaultNotificationService
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNotificationService implements NotificationService
{
    public static final String SETTING_NOTIFICATION_SOURCE = "notificationSource";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultNotificationService.class);
    private static final String EVENT_QUEUE_SPRING_BEAN = "cockpitEventQueue";


    @Override
    public void notifyUser(final String source, final String eventType,
                    final com.hybris.cockpitng.util.notifications.event.NotificationEvent.Level level, final Object... referenceObjects)
    {
        try
        {
            final CockpitEventQueue cockpitEventQueue = (CockpitEventQueue)SpringUtil.getBean(EVENT_QUEUE_SPRING_BEAN);
            if(cockpitEventQueue != null)
            {
                cockpitEventQueue.publishEvent(new com.hybris.cockpitng.util.notifications.event.NotificationEvent(source, eventType,
                                level, referenceObjects));
            }
        }
        catch(final UiException e)
        {
            final String errorMessage = "NotificationUtils can be called only under ZK environment!";
            if(LOG.isDebugEnabled())
            {
                LOG.warn(errorMessage, e);
            }
            else
            {
                LOG.warn(errorMessage);
            }
        }
    }


    @Override
    public void clearNotifications(final String source)
    {
        try
        {
            final CockpitEventQueue cockpitEventQueue = (CockpitEventQueue)SpringUtil.getBean(EVENT_QUEUE_SPRING_BEAN);
            if(cockpitEventQueue != null)
            {
                cockpitEventQueue
                                .publishEvent(new com.hybris.cockpitng.util.notifications.event.ClearNotificationsEvent(source, null));
            }
        }
        catch(final UiException e)
        {
            final String errorMessage = "NotificationUtils can be called only under ZK environment!";
            if(LOG.isDebugEnabled())
            {
                LOG.warn(errorMessage, e);
            }
            else
            {
                LOG.warn(errorMessage);
            }
        }
    }


    @Override
    public void clearNotifications(final String source,
                    final com.hybris.cockpitng.util.notifications.event.NotificationEvent.Level level)
    {
        try
        {
            final CockpitEventQueue cockpitEventQueue = (CockpitEventQueue)SpringUtil.getBean(EVENT_QUEUE_SPRING_BEAN);
            if(cockpitEventQueue != null)
            {
                cockpitEventQueue
                                .publishEvent(new com.hybris.cockpitng.util.notifications.event.ClearNotificationsEvent(source, level));
            }
        }
        catch(final UiException e)
        {
            final String errorMessage = "NotificationUtils can be called only under ZK environment!";
            if(LOG.isDebugEnabled())
            {
                LOG.warn(errorMessage, e);
            }
            else
            {
                LOG.warn(errorMessage);
            }
        }
    }


    @Override
    public String getWidgetNotificationSource(final WidgetInstanceManager widgetInstanceManager)
    {
        final String widgetId = widgetInstanceManager != null && widgetInstanceManager.getWidgetslot() != null
                        ? widgetInstanceManager.getWidgetslot().getWidgetInstance().getId()
                        : com.hybris.cockpitng.util.notifications.event.NotificationEvent.EVENT_SOURCE_UNKNOWN;
        return widgetInstanceManager != null
                        ? (String)widgetInstanceManager.getWidgetSettings().getOrDefault(SETTING_NOTIFICATION_SOURCE,
                        widgetId)
                        : widgetId;
    }


    @Override
    public String getWidgetNotificationSource(final ActionContext<?> context)
    {
        final String parameter = (String)context.getParameter(SETTING_NOTIFICATION_SOURCE);
        return StringUtils.isNotBlank(parameter) ? parameter : context.getCode();
    }
}
