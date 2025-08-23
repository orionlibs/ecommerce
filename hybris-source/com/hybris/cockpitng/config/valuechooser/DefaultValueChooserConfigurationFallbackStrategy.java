/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.valuechooser;

import com.hybris.cockpitng.config.valuechooser.jaxb.ValueChooser;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultValueChooserConfigurationFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<ValueChooser>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultValueChooserConfigurationFallbackStrategy.class);


    @Override
    public ValueChooser loadFallbackConfiguration(final ConfigContext context, final Class<ValueChooser> configurationType)
    {
        LOG.debug("Fallback for {} has been called", DefaultValueChooserConfigurationFallbackStrategy.class);
        return new ValueChooser();
    }
}
