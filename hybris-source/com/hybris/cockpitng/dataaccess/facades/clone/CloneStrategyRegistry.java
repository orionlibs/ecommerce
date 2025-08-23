/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.clone;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import java.util.List;
import org.springframework.core.OrderComparator;

/**
 * Extension {@link AbstractStrategyRegistry} that can handle {@link CloneStrategy}s.
 */
public class CloneStrategyRegistry extends AbstractStrategyRegistry<CloneStrategy, Object>
{
    @Override
    public boolean canHandle(final CloneStrategy strategy, final Object context)
    {
        return strategy.canHandle(context);
    }


    @Override
    protected List<CloneStrategy> getOrderedStrategies(final Object context, final Context additionalContext)
    {
        getStrategies().ifPresent(OrderComparator::sort);
        return super.getOrderedStrategies(context, additionalContext);
    }
}
