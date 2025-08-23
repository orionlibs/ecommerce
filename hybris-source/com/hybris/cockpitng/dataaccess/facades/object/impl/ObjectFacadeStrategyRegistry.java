/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.impl;

import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeStrategy;

/**
 * Extension {@link AbstractStrategyRegistry} that can handle {@link ObjectFacadeStrategy}s.
 */
public class ObjectFacadeStrategyRegistry extends AbstractStrategyRegistry<ObjectFacadeStrategy, Object>
{
    @Override
    public boolean canHandle(final ObjectFacadeStrategy strategy, final Object context)
    {
        return strategy.canHandle(context);
    }
}
