/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.searchadapters.conditions.products;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link SearchConditionAdapter} responsible for handling node representing category. The handler gets all
 * subcategories of given category and add them and itself as a condition to {@link AdvancedSearchData}.
 */
public class CategoryConditionAdapter extends SearchConditionAdapter
{
    private static final String CONFIG_BACKOFFICE_SEARCH_REVERSE_CATEGORY_INDEX_LOOKUP_ENABLED = "backoffice.search.reverse.category.index.lookup.enabled";
    private String categoryPropertyName;
    private String categoriesPropertyName;
    private ValueComparisonOperator operator;
    private ConfigurationService configurationService;


    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node.getData() instanceof CategoryModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        if(isReverseCategoryIndexLookupEnabled())
        {
            final CategoryModel category = (CategoryModel)node.getData();
            final SearchConditionData condition = createSearchConditions(categoriesPropertyName, category.getPk(), operator);
            searchData.addCondition(condition.getFieldType(), condition.getOperator(), condition.getValue());
        }
        else
        {
            final CategoryModel category = (CategoryModel)node.getData();
            final Stream<CategoryModel> categoryStream = appendToStream(category.getAllSubcategories().stream(), category);
            final List<SearchConditionData> conditions = categoryStream
                            .map(subcategory -> createSearchConditions(categoryPropertyName, subcategory.getPk(), operator))
                            .collect(Collectors.toList());
            searchData.addConditionList(ValueComparisonOperator.OR, conditions);
        }
    }


    protected boolean isReverseCategoryIndexLookupEnabled()
    {
        return configurationService.getConfiguration().getBoolean(CONFIG_BACKOFFICE_SEARCH_REVERSE_CATEGORY_INDEX_LOOKUP_ENABLED, false);
    }


    protected Stream<CategoryModel> appendToStream(final Stream<CategoryModel> stream, final CategoryModel categoryToAdd)
    {
        return Stream.concat(stream, Stream.of(categoryToAdd));
    }


    @Required
    public void setCategoryPropertyName(final String categoryPropertyName)
    {
        this.categoryPropertyName = categoryPropertyName;
    }


    @Required
    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }


    @Required
    public void setCategoriesPropertyName(final String categoriesPropertyName)
    {
        this.categoriesPropertyName = categoriesPropertyName;
    }


    @Required
    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
