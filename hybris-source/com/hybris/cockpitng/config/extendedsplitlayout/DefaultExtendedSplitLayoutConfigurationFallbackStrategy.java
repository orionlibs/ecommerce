/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.extendedsplitlayout;

import com.hybris.cockpitng.config.extendedsplitlayout.jaxb.ExtendedSplitLayout;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;

public class DefaultExtendedSplitLayoutConfigurationFallbackStrategy extends
                AbstractCockpitConfigurationFallbackStrategy<ExtendedSplitLayout>
{
    @Override
    public ExtendedSplitLayout loadFallbackConfiguration(final ConfigContext context,
                    final Class<ExtendedSplitLayout> configurationType)
    {
        return new ExtendedSplitLayout();
    }
}
