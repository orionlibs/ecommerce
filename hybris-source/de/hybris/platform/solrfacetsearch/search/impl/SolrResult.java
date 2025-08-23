package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.solr.common.SolrDocument;

public class SolrResult
{
    private final SolrDocument document;
    private final SearchQuery query;


    public SolrResult(SolrDocument document, SearchQuery query)
    {
        this.document = document;
        this.query = query;
    }


    public SolrDocument getDocument()
    {
        return this.document;
    }


    public SearchQuery getQuery()
    {
        return this.query;
    }
}
