/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing classification catalog.
 * The handler gets classification system pk and add it as a condition to {@link AdvancedSearchData}.
 */
public class FullTextSearchClassificationSystemConditionAdapter extends SearchConditionAdapter
{
    private String classificationSystemPropertyName;
    private ValueComparisonOperator operator;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node.getData() instanceof ClassificationSystemModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final ClassificationSystemModel classificationSystem = (ClassificationSystemModel)node.getData();
        final SearchConditionData catalogCondition = createSearchConditions(classificationSystemPropertyName,
                        classificationSystem.getPk(), operator);
        searchData.addCondition(catalogCondition.getFieldType(), catalogCondition.getOperator(), catalogCondition.getValue());
    }


    public void setClassificationSystemPropertyName(final String classificationSystemPropertyName)
    {
        this.classificationSystemPropertyName = classificationSystemPropertyName;
    }


    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
