package de.hybris.platform.solrfacetsearch.search.context.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFacetSearchContext implements FacetSearchContext
{
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;
    private SearchQuery searchQuery;
    private SearchResult searchResult;
    private Collection<CatalogVersionModel> parentSessionCatalogVersions;
    private final Map<String, String> searchHints = new HashMap<>();
    private FacetSearchContext.Status status;
    private final Map<String, Object> attributes = new HashMap<>();
    private final List<Exception> failureExceptions = new ArrayList<>();
    private List<IndexedTypeSort> availableNamedSorts = new ArrayList<>();
    private IndexedTypeSort namedSort;


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setFacetSearchConfig(FacetSearchConfig facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public IndexedType getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedType(IndexedType indexedType)
    {
        this.indexedType = indexedType;
    }


    public SearchQuery getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setSearchQuery(SearchQuery searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SearchResult getSearchResult()
    {
        return this.searchResult;
    }


    public void setSearchResult(SearchResult searchResult)
    {
        this.searchResult = searchResult;
    }


    public Collection<CatalogVersionModel> getParentSessionCatalogVersions()
    {
        return this.parentSessionCatalogVersions;
    }


    public void setParentSessionCatalogVersions(Collection<CatalogVersionModel> parentSessionCatalogVersions)
    {
        this.parentSessionCatalogVersions = parentSessionCatalogVersions;
    }


    public Map<String, String> getSearchHints()
    {
        return this.searchHints;
    }


    public FacetSearchContext.Status getStatus()
    {
        return this.status;
    }


    public void setStatus(FacetSearchContext.Status status)
    {
        this.status = status;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public void addFailureException(Exception exception)
    {
        this.failureExceptions.add(exception);
    }


    public List<Exception> getFailureExceptions()
    {
        return this.failureExceptions;
    }


    public List<IndexedTypeSort> getAvailableNamedSorts()
    {
        return this.availableNamedSorts;
    }


    public void setAvailableNamedSorts(List<IndexedTypeSort> availableNamedSorts)
    {
        this.availableNamedSorts = availableNamedSorts;
    }


    public IndexedTypeSort getNamedSort()
    {
        return this.namedSort;
    }


    public void setNamedSort(IndexedTypeSort namedSort)
    {
        this.namedSort = namedSort;
    }
}
