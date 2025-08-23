/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.CatalogModel;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing catalog. The handler gets catalog pk and
 * adds it to {@link AdvancedSearchData}.
 */
public class CatalogConditionAdapter extends SearchConditionAdapter
{
    private String catalogPropertyName;
    private ValueComparisonOperator operator;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node.getData() instanceof CatalogModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final CatalogModel catalog = (CatalogModel)node.getData();
        final SearchConditionData searchCondition = createSearchConditions(catalogPropertyName, catalog.getPk(), operator);
        searchData.addCondition(searchCondition.getFieldType(), searchCondition.getOperator(), searchCondition.getValue());
    }


    @Required
    public void setCatalogPropertyName(final String catalogPropertyName)
    {
        this.catalogPropertyName = catalogPropertyName;
    }


    @Required
    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
