package de.hybris.platform.solrfacetsearch.search.context;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FacetSearchContext
{
    FacetSearchConfig getFacetSearchConfig();


    IndexedType getIndexedType();


    SearchQuery getSearchQuery();


    SearchResult getSearchResult();


    void setSearchResult(SearchResult paramSearchResult);


    Collection<CatalogVersionModel> getParentSessionCatalogVersions();


    Map<String, String> getSearchHints();


    Status getStatus();


    List<Exception> getFailureExceptions();


    Map<String, Object> getAttributes();


    List<IndexedTypeSort> getAvailableNamedSorts();


    IndexedTypeSort getNamedSort();


    void setNamedSort(IndexedTypeSort paramIndexedTypeSort);
}
