/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing classification catalog.
 * The handler gets all catalog's versions, then for each catalog's version it gets all categories and add them as a
 * condition to {@link AdvancedSearchData}.
 */
public class FlexibleSearchClassificationSystemConditionAdapter extends SearchConditionAdapter
{
    private String classificationSystemVersionPropertyName;
    private ValueComparisonOperator operator;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node.getData() instanceof ClassificationSystemModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)node.getData();
        final Stream<CategoryModel> rootCategories = classificationSystemModel.getCatalogVersions().stream()
                        .flatMap(catalogVersion -> catalogVersion.getRootCategories().stream());
        final Stream<CategoryModel> allSubcategories = classificationSystemModel.getCatalogVersions().stream()
                        .flatMap(catalogVersion -> catalogVersion.getRootCategories().stream())
                        .flatMap(rootCategory -> rootCategory.getAllSubcategories().stream());
        final List<SearchConditionData> conditions = Stream.concat(rootCategories, allSubcategories).distinct()
                        .map(subcategory -> createSearchConditions(classificationSystemVersionPropertyName, subcategory.getPk(), operator))
                        .collect(Collectors.toList());
        searchData.addConditionList(ValueComparisonOperator.OR, conditions);
    }


    @Required
    public void setClassificationSystemVersionPropertyName(final String classificationSystemVersionPropertyName)
    {
        this.classificationSystemVersionPropertyName = classificationSystemVersionPropertyName;
    }


    @Required
    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
