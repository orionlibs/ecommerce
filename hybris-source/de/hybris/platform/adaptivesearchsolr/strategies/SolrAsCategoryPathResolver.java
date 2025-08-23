package de.hybris.platform.adaptivesearchsolr.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;

@FunctionalInterface
public interface SolrAsCategoryPathResolver
{
    List<CategoryModel> resolveCategoryPath(SearchQuery paramSearchQuery, List<CatalogVersionModel> paramList);
}
