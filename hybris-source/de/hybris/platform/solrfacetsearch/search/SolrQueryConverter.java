package de.hybris.platform.solrfacetsearch.search;

import org.apache.solr.client.solrj.SolrQuery;

@Deprecated(since = "5.7")
public interface SolrQueryConverter
{
    SolrQuery convertSolrQuery(SearchQuery paramSearchQuery) throws FacetSearchException;
}
