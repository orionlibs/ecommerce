/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing classification catalog version.
 * The handler gets classification system version's pk and add it as a condition to {@link AdvancedSearchData}.
 */
public class FullTextSearchClassificationSystemVersionConditionAdapter extends SearchConditionAdapter
{
    private String classificationSystemVersionPropertyName;
    private ValueComparisonOperator operator;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node.getData() instanceof ClassificationSystemVersionModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)node.getData();
        final SearchConditionData catalogCondition = createSearchConditions(classificationSystemVersionPropertyName,
                        classificationSystemVersion.getPk(), operator);
        searchData.addCondition(catalogCondition.getFieldType(), catalogCondition.getOperator(), catalogCondition.getValue());
    }


    public void setClassificationSystemVersionPropertyName(final String classificationSystemVersionPropertyName)
    {
        this.classificationSystemVersionPropertyName = classificationSystemVersionPropertyName;
    }


    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
