package de.hybris.platform.adaptivesearchsolr.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;

@FunctionalInterface
public interface SolrAsCatalogVersionResolver
{
    List<CatalogVersionModel> resolveCatalogVersions(SearchQuery paramSearchQuery);
}
