package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import java.util.List;
import java.util.Optional;

public interface AsCategoryService
{
    void setCurrentCategoryPath(List<CategoryModel> paramList);


    Optional<List<CategoryModel>> getCurrentCategoryPath();


    void clearCurrentCategoryPath();


    List<CategoryModel> buildCategoryPath(List<String> paramList, List<CatalogVersionModel> paramList1, boolean paramBoolean);


    List<CategoryModel> getAllCategoriesForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    List<List<PK>> getAllCategoryRelationsForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);
}
