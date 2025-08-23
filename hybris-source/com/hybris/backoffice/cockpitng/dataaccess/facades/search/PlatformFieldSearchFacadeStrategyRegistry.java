/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.search;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.search.impl.FieldSearchFacadeStrategyRegistry;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.core.OrderComparator;

public class PlatformFieldSearchFacadeStrategyRegistry extends FieldSearchFacadeStrategyRegistry
{
    protected void loadAndSortStrategies()
    {
        final Map<String, FieldSearchFacadeStrategy> strategyMap = BackofficeSpringUtil
                        .getAllBeans(FieldSearchFacadeStrategy.class);
        final List strategies = new ArrayList<>(strategyMap.values());
        Collections.sort(strategies, OrderComparator.INSTANCE);
        setStrategies(strategies);
    }


    @Override
    public FieldSearchFacadeStrategy<?> getStrategy(final String context, final Context additionalContext)
    {
        if(!getStrategies().isPresent())
        {
            loadAndSortStrategies();
        }
        return super.getStrategy(context, additionalContext);
    }
}
