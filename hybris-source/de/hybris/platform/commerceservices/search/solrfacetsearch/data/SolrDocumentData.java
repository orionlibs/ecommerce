package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;

public class SolrDocumentData<SEARCH_QUERY_TYPE, SOLR_DOCUMENT_TYPE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SEARCH_QUERY_TYPE searchQuery;
    private SOLR_DOCUMENT_TYPE solrDocument;


    public void setSearchQuery(SEARCH_QUERY_TYPE searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SEARCH_QUERY_TYPE getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setSolrDocument(SOLR_DOCUMENT_TYPE solrDocument)
    {
        this.solrDocument = solrDocument;
    }


    public SOLR_DOCUMENT_TYPE getSolrDocument()
    {
        return this.solrDocument;
    }
}
