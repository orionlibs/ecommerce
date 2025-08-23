package de.hybris.platform.productcockpit.services.catalog;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface CatalogListService extends CatalogService
{
    boolean assignAndRemoveProducts(Collection<TypedObject> paramCollection, CategoryModel paramCategoryModel, List<CategoryModel> paramList);


    boolean assignAndRemoveCategories(Collection<TypedObject> paramCollection, CategoryModel paramCategoryModel);
}
