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
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductInCategoryConditionAdapter extends SearchConditionAdapter
{
    @Override
    public boolean canHandle(final NavigationNode node)
    {
        return node != null && node.getData() instanceof CategoryModel;
    }


    @Override
    public void addSearchCondition(final AdvancedSearchData searchData, final NavigationNode node)
    {
        final CategoryModel category = (CategoryModel)node.getData();
        final ArrayList<CategoryModel> categories = new ArrayList<>(category.getAllSubcategories());
        categories.add(category);
        final List<PK> elements = categories.stream().map(cat -> cat.getPk()).collect(Collectors.toList());
        final SearchConditionData condition = createSearchConditions(ProductModel.SUPERCATEGORIES, elements,
                        ValueComparisonOperator.IN);
        searchData.addCondition(condition.getFieldType(), condition.getOperator(), elements);
    }
}
