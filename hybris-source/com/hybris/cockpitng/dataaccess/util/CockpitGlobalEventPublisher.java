/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.util;

import com.hybris.cockpitng.dataaccess.context.Context;

/**
 * Utility interface to publish global events
 *
 */
public interface CockpitGlobalEventPublisher
{
    String SETTING_COCKPIT_EVENT_NOTIFICATION = "cockpit.globalevent.notification.enabled";
    String CTX_COCKPIT_EVENT_NOTIFICATION = "notificationEnabled";


    void publish(final Object eventSource, final String eventName, final Object eventData, final Context eventContext);


    void publish(final String eventName, final Object eventData, final Context eventContext);
}
