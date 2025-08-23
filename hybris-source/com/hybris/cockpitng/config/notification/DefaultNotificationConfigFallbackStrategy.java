/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification;

import com.hybris.cockpitng.config.notification.jaxb.NotificationArea;
import com.hybris.cockpitng.config.notification.jaxb.NotificationDefaults;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNotificationConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<NotificationArea>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultNotificationConfigFallbackStrategy.class);
    /**
     * @deprecated since 6.6 - not used anymore
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String FALLBACK_NOTIFICATION_RENDERER = "fallbackNotificationRenderer";


    @Override
    public NotificationArea loadFallbackConfiguration(final ConfigContext context, final Class<NotificationArea> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", DefaultNotificationConfigFallbackStrategy.class);
        }
        final NotificationArea fallbackConfig = new NotificationArea();
        fallbackConfig.setDefaults(new NotificationDefaults());
        fallbackConfig.getDefaults().setLinksEnabled(Boolean.FALSE);
        return fallbackConfig;
    }
}
