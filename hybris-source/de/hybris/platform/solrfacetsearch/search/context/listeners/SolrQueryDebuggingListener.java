package de.hybris.platform.solrfacetsearch.search.context.listeners;

import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;
import org.apache.log4j.Logger;
import org.apache.solr.common.util.SimpleOrderedMap;

public class SolrQueryDebuggingListener implements FacetSearchListener
{
    private static final Logger LOG = Logger.getLogger(SolrQueryDebuggingListener.class);


    public void beforeSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
        facetSearchContext.getSearchQuery().addRawParam("debugQuery", new String[] {"true"});
    }


    public void afterSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
        LOG.info("Raw Query: " + ((SimpleOrderedMap)facetSearchContext.getSearchResult().getSolrObject().getResponse().get("debug")).get("rawquerystring"));
        LOG.info("Parsed Solr Query: " + ((SimpleOrderedMap)facetSearchContext.getSearchResult().getSolrObject().getResponse().get("debug")).get("parsedquery"));
        LOG.info("Filter Queries: " + ((SimpleOrderedMap)facetSearchContext.getSearchResult().getSolrObject().getResponse().get("debug")).get("filter_queries"));
        LOG.info("Solr Query explanation: " + ((SimpleOrderedMap)facetSearchContext.getSearchResult().getSolrObject().getResponse().get("debug")).get("explain"));
    }


    public void afterSearchError(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
    }
}
