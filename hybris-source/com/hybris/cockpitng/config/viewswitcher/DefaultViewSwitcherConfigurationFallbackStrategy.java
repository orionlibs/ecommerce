/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.viewswitcher;

import com.hybris.cockpitng.config.viewswitcher.jaxb.ViewSwitcher;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;

public class DefaultViewSwitcherConfigurationFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<ViewSwitcher>
{
    @Override
    public ViewSwitcher loadFallbackConfiguration(final ConfigContext context, final Class<ViewSwitcher> configurationType)
    {
        return new ViewSwitcher();
    }
}
