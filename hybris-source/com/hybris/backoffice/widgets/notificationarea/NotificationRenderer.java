/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * A renderer bean capable of showing notification on specified container
 *
 */
public interface NotificationRenderer
{
    /**
     * Creates component that represents provided notification
     *
     * @param event event to be rendered
     * @param parameters additional parameters from event configuration
     */
    Component render(final NotificationEvent event, final Map<String, Object> parameters);
}
