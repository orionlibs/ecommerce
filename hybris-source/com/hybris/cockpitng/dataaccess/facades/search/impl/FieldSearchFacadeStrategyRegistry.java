/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search.impl;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Registry holding instances of {@link com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy} and a
 * default one.
 */
public class FieldSearchFacadeStrategyRegistry extends AbstractStrategyRegistry<FieldSearchFacadeStrategy<?>, String>
{
    public static final String CONTEXT_ATTR_PREFERRED_STRATEGY_NAME = "preferredStrategyName";


    @Override
    public boolean canHandle(final FieldSearchFacadeStrategy<?> strategy, final String context, final Context additionalContext)
    {
        return strategy.canHandle(context, additionalContext);
    }


    @Override
    public boolean canHandle(final FieldSearchFacadeStrategy<?> strategy, final String context)
    {
        return canHandle(strategy, context, new DefaultContext());
    }


    @Override
    public FieldSearchFacadeStrategy<?> getStrategy(final String context, final Context additionalContext)
    {
        if(additionalContext != null)
        {
            final List preferredStrategies = findPreferredStrategies(context, additionalContext);
            if(preferredStrategies.isEmpty())
            {
                additionalContext.removeAttribute(CONTEXT_ATTR_PREFERRED_STRATEGY_NAME);
            }
        }
        return super.getStrategy(context, additionalContext);
    }


    @Override
    protected Optional<FieldSearchFacadeStrategy<?>> findPreferredStrategy(final String context, final Context additionalContext)
    {
        final String preferredStrategyName = (String)additionalContext.getAttribute(CONTEXT_ATTR_PREFERRED_STRATEGY_NAME);
        final Optional<List<FieldSearchFacadeStrategy<?>>> strategies = getStrategies();
        if(strategies.isPresent() && StringUtils.isNotBlank(preferredStrategyName))
        {
            return strategies.get().stream().filter(s -> preferredStrategyName.equalsIgnoreCase(s.getStrategyName())).findFirst();
        }
        return Optional.empty();
    }


    @Override
    protected List<FieldSearchFacadeStrategy<?>> findPreferredStrategies(final String context, final Context additionalContext)
    {
        final String preferredStrategyName = (String)additionalContext.getAttribute(CONTEXT_ATTR_PREFERRED_STRATEGY_NAME);
        final Optional<List<FieldSearchFacadeStrategy<?>>> strategies = getStrategies();
        if(strategies.isPresent() && StringUtils.isNotBlank(preferredStrategyName))
        {
            return strategies.get().stream().filter(s -> preferredStrategyName.equalsIgnoreCase(s.getStrategyName()))
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
