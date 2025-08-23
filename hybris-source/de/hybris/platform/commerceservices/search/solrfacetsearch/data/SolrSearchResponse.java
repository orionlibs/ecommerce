package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;

public class SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request;
    private SEARCH_RESULT_TYPE searchResult;


    public void setRequest(SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request)
    {
        this.request = request;
    }


    public SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> getRequest()
    {
        return this.request;
    }


    public void setSearchResult(SEARCH_RESULT_TYPE searchResult)
    {
        this.searchResult = searchResult;
    }


    public SEARCH_RESULT_TYPE getSearchResult()
    {
        return this.searchResult;
    }
}
