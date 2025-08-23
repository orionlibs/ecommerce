package com.hybris.backoffice.solrsearch.providers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;

public interface ProductCategoryAssignmentResolver
{
    Collection<CategoryModel> getIndexedCategories(ProductModel paramProductModel);
}
