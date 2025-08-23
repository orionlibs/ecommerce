package com.hybris.backoffice.solrsearch.providers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultAllProductCategoryAssignmentResolver extends DefaultProductCategoryAssignmentResolver
{
    public Set<CategoryModel> getIndexedCategories(ProductModel product)
    {
        Set<CategoryModel> categories = new HashSet<>(super.getIndexedCategories(product));
        categories.addAll(resolveSuperCategories(categories));
        return categories;
    }


    protected Collection<CategoryModel> resolveSuperCategories(Collection<CategoryModel> categories)
    {
        Set<CategoryModel> result = new HashSet<>();
        for(CategoryModel category : categories)
        {
            result.addAll(category.getAllSupercategories());
        }
        return result;
    }
}
