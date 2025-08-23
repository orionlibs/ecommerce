package com.hybris.backoffice.solrsearch.providers.impl;

import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultProductCategoryAssignmentResolver implements ProductCategoryAssignmentResolver
{
    public Set<CategoryModel> getIndexedCategories(ProductModel product)
    {
        Set<CategoryModel> categories = new LinkedHashSet<>(product.getSupercategories());
        if(product instanceof VariantProductModel)
        {
            ProductModel baseProduct = ((VariantProductModel)product).getBaseProduct();
            categories.addAll(baseProduct.getSupercategories());
        }
        return categories;
    }
}
