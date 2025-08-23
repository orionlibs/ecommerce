/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type.impl;

import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacadeStrategy;

/**
 * Extension of {@link AbstractStrategyRegistry} that can handle {@link TypeFacadeStrategy}s.
 */
public class TypeFacadeStrategyRegistry extends AbstractStrategyRegistry<TypeFacadeStrategy, String>
{
    @Override
    public boolean canHandle(final TypeFacadeStrategy strategy, final String context)
    {
        return strategy.canHandle(context);
    }
}
