/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ObjectFactory;

/**
 * Fallback configuration strategy for {@link DynamicForms} This strategy creates empty configuration for dynamic forms
 */
public class DefaultDynamicFormsConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<DynamicForms>
{
    @Override
    public DynamicForms loadFallbackConfiguration(final ConfigContext context, final Class<DynamicForms> configurationType)
    {
        return new ObjectFactory().createDynamicForms();
    }
}
