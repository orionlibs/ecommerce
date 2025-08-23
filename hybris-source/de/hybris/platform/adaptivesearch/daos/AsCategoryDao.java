package de.hybris.platform.adaptivesearch.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import java.util.List;

public interface AsCategoryDao
{
    List<CategoryModel> findCategoriesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    List<List<PK>> findCategoryRelationsByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);
}
