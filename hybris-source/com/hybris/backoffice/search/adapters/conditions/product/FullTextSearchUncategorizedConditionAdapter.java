/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.adapters.conditions.product;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.products.UncategorizedConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;

/**
 * {@link UncategorizedConditionAdapter} responsible for handling node representing uncategorized products.
 * The handler adds condition to {@link AdvancedSearchData} that product doesn't have assigned any category.
 * Moreover it finds and invokes handler for parent object which may represents CatalogModel or CatalogVersionModel
 */
public class FullTextSearchUncategorizedConditionAdapter extends UncategorizedConditionAdapter
{
    private String uncategorizedPropertyName;
    private ValueComparisonOperator operator;


    protected SearchConditionData buildUncategorizedSearchCondition()
    {
        return createSearchConditions(uncategorizedPropertyName, Boolean.TRUE, operator);
    }


    public void setUncategorizedPropertyName(final String uncategorizedPropertyName)
    {
        this.uncategorizedPropertyName = uncategorizedPropertyName;
    }


    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
