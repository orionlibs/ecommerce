/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.quicklist;

import com.hybris.cockpitng.config.quicklist.jaxb.QuickList;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultQuickListConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<QuickList>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultQuickListConfigFallbackStrategy.class);


    @Override
    public QuickList loadFallbackConfiguration(final ConfigContext context, final Class<QuickList> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", DefaultQuickListConfigFallbackStrategy.class);
        }
        final QuickList fallbackConfiguration = new QuickList();
        fallbackConfiguration.setDisplayDefaultImage(Boolean.TRUE);
        return fallbackConfiguration;
    }
}
