package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import java.io.Serializable;
import java.util.List;

public class SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SolrSearchQueryData searchQueryData;
    private PageableData pageableData;
    private List<CatalogVersionModel> catalogVersions;
    private FACET_SEARCH_CONFIG_TYPE facetSearchConfig;
    private INDEXED_TYPE_TYPE indexedType;
    private List<IndexedPropertyValueData<INDEXED_PROPERTY_TYPE>> indexedPropertyValues;
    private INDEXED_TYPE_SORT_TYPE currentSort;
    private String searchText;
    private SEARCH_QUERY_TYPE searchQuery;


    public void setSearchQueryData(SolrSearchQueryData searchQueryData)
    {
        this.searchQueryData = searchQueryData;
    }


    public SolrSearchQueryData getSearchQueryData()
    {
        return this.searchQueryData;
    }


    public void setPageableData(PageableData pageableData)
    {
        this.pageableData = pageableData;
    }


    public PageableData getPageableData()
    {
        return this.pageableData;
    }


    public void setCatalogVersions(List<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public List<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    public void setFacetSearchConfig(FACET_SEARCH_CONFIG_TYPE facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public FACET_SEARCH_CONFIG_TYPE getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setIndexedType(INDEXED_TYPE_TYPE indexedType)
    {
        this.indexedType = indexedType;
    }


    public INDEXED_TYPE_TYPE getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedPropertyValues(List<IndexedPropertyValueData<INDEXED_PROPERTY_TYPE>> indexedPropertyValues)
    {
        this.indexedPropertyValues = indexedPropertyValues;
    }


    public List<IndexedPropertyValueData<INDEXED_PROPERTY_TYPE>> getIndexedPropertyValues()
    {
        return this.indexedPropertyValues;
    }


    public void setCurrentSort(INDEXED_TYPE_SORT_TYPE currentSort)
    {
        this.currentSort = currentSort;
    }


    public INDEXED_TYPE_SORT_TYPE getCurrentSort()
    {
        return this.currentSort;
    }


    public void setSearchText(String searchText)
    {
        this.searchText = searchText;
    }


    public String getSearchText()
    {
        return this.searchText;
    }


    public void setSearchQuery(SEARCH_QUERY_TYPE searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SEARCH_QUERY_TYPE getSearchQuery()
    {
        return this.searchQuery;
    }
}
