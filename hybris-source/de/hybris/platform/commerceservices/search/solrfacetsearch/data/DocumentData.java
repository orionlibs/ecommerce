package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;
import java.util.List;

public class DocumentData<SEARCH_QUERY_TYPE, DOCUMENT_TYPE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SEARCH_QUERY_TYPE searchQuery;
    private DOCUMENT_TYPE document;
    private List<DOCUMENT_TYPE> variants;


    public void setSearchQuery(SEARCH_QUERY_TYPE searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SEARCH_QUERY_TYPE getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setDocument(DOCUMENT_TYPE document)
    {
        this.document = document;
    }


    public DOCUMENT_TYPE getDocument()
    {
        return this.document;
    }


    public void setVariants(List<DOCUMENT_TYPE> variants)
    {
        this.variants = variants;
    }


    public List<DOCUMENT_TYPE> getVariants()
    {
        return this.variants;
    }
}
