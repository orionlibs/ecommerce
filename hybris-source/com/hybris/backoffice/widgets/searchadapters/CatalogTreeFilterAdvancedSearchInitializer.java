/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Advanced search initialized responsible for translating object properties (connected with catalogs) to flexible
 * search properties.
 */
public class CatalogTreeFilterAdvancedSearchInitializer implements AdvancedSearchInitializer
{
    private List<SearchConditionAdapter> conditionsAdapters;


    @Override
    public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode)
    {
        if(navigationNode.isPresent())
        {
            final NavigationNode node = navigationNode.get();
            conditionsAdapters.stream().filter(adapter -> adapter.canHandle(node)).findFirst()
                            .ifPresent(adapter -> adapter.addSearchCondition(searchData, node));
        }
    }


    public List<SearchConditionAdapter> getConditionsAdapters()
    {
        return conditionsAdapters;
    }


    @Required
    public void setConditionsAdapters(final List<SearchConditionAdapter> conditionsAdapters)
    {
        this.conditionsAdapters = conditionsAdapters;
    }
}
