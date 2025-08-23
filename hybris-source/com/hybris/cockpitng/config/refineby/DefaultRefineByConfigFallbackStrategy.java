/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby;

import com.hybris.cockpitng.config.refineby.jaxb.FacetConfig;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRefineByConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<FacetConfig>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRefineByConfigFallbackStrategy.class);


    @Override
    public FacetConfig loadFallbackConfiguration(final ConfigContext context, final Class<FacetConfig> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", DefaultRefineByConfigFallbackStrategy.class);
        }
        final FacetConfig fallbackConfiguration = new FacetConfig();
        return fallbackConfiguration;
    }
}
