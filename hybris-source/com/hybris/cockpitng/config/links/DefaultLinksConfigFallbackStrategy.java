/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.links;

import com.hybris.cockpitng.config.links.jaxb.Links;
import com.hybris.cockpitng.config.refineby.DefaultRefineByConfigFallbackStrategy;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLinksConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<Links>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRefineByConfigFallbackStrategy.class);


    @Override
    public Links loadFallbackConfiguration(final ConfigContext context, final Class<Links> configurationType)
    {
        LOG.debug("Fallback for {} has been called", Links.class);
        return new Links();
    }
}
