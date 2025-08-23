/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGridViewConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<GridView>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGridViewConfigFallbackStrategy.class);


    @Override
    public GridView loadFallbackConfiguration(final ConfigContext context, final Class<GridView> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", DefaultGridViewConfigFallbackStrategy.class);
        }
        final GridView fallbackConfiguration = new GridView();
        fallbackConfiguration.setDisplayDefaultImage(Boolean.TRUE);
        return fallbackConfiguration;
    }
}
