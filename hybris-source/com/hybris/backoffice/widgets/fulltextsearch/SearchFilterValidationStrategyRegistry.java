/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extension of {@link AbstractStrategyRegistry} that can handle {@link SearchFilterValidationStrategy}s.
 */
public class SearchFilterValidationStrategyRegistry extends AbstractStrategyRegistry<SearchFilterValidationStrategy, String>
{
    @Override
    public boolean canHandle(final SearchFilterValidationStrategy strategy, final String context)
    {
        return strategy.canHandle(context);
    }


    @Override
    public SearchFilterValidationStrategy getStrategy(final String context)
    {
        final Optional<List<SearchFilterValidationStrategy>> optionalStrategiesList = getStrategies();
        if(optionalStrategiesList.isPresent())
        {
            return optionalStrategiesList.get().stream().filter(strategy -> strategy.canHandle(context)).findFirst()
                            .orElse(getDefaultStrategy());
        }
        return getDefaultStrategy();
    }


    @Required
    @Override
    public void setDefaultStrategy(final SearchFilterValidationStrategy defaultStrategy)
    {
        super.setDefaultStrategy(defaultStrategy);
    }
}
