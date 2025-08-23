/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.draganddrop.DragAndDrop;

public class DefaultDragAndDropConfigurationFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<DragAndDrop>
{
    @Override
    public DragAndDrop loadFallbackConfiguration(final ConfigContext context, final Class<DragAndDrop> configurationType)
    {
        return new DragAndDrop();
    }
}
