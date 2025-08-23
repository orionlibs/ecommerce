package de.hybris.platform.adaptivesearch.searchservices.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import java.util.List;

@FunctionalInterface
public interface SnAsCategoryPathResolver
{
    List<CategoryModel> resolveCategoryPath(SnSearchContext paramSnSearchContext, List<CatalogVersionModel> paramList);
}
