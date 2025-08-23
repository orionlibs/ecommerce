package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StopWatch;

public class DefaultIndexerQueriesExecutor implements IndexerQueriesExecutor
{
    private static final Logger LOG = Logger.getLogger(DefaultIndexerQueriesExecutor.class);
    private FlexibleSearchService flexibleSearchService;
    private IndexerQueryContextFactory<IndexerQueryContext> indexerQueryContextFactory;


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public IndexerQueryContextFactory<IndexerQueryContext> getIndexerQueryContextFactory()
    {
        return this.indexerQueryContextFactory;
    }


    @Required
    public void setIndexerQueryContextFactory(IndexerQueryContextFactory<IndexerQueryContext> indexerQueryContextFactory)
    {
        this.indexerQueryContextFactory = indexerQueryContextFactory;
    }


    public List<PK> getPks(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String query, Map<String, Object> queryParameters) throws IndexerException
    {
        try
        {
            this.indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query, queryParameters);
            this.indexerQueryContextFactory.initializeContext();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Query: " + query);
            }
            StopWatch timer = new StopWatch();
            timer.start();
            FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query, queryParameters);
            fsQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class}));
            SearchResult<PK> fsResult = this.flexibleSearchService.search(fsQuery);
            timer.stop();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Number of pks: " + fsResult.getTotalCount() + ", query time: " + timer.getTotalTimeSeconds() + "s.");
            }
            this.indexerQueryContextFactory.destroyContext();
            return fsResult.getResult();
        }
        catch(IndexerException | RuntimeException e)
        {
            this.indexerQueryContextFactory.destroyContext(e);
            throw e;
        }
    }


    public List<ItemModel> getItems(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<PK> pks) throws IndexerException
    {
        String query = "SELECT {pk} FROM {" + indexedType.getCode() + "} where {pk} in (?pks)";
        Map<String, Object> queryParameters = Collections.singletonMap("pks", pks);
        try
        {
            this.indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query, queryParameters);
            this.indexerQueryContextFactory.initializeContext();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Query: " + query);
            }
            StopWatch timer = new StopWatch();
            timer.start();
            FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query, queryParameters);
            SearchResult<ItemModel> fsResult = this.flexibleSearchService.search(fsQuery);
            timer.stop();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Number of items: " + fsResult.getTotalCount() + ", query time: " + timer.getTotalTimeSeconds() + "s.");
            }
            this.indexerQueryContextFactory.destroyContext();
            return fsResult.getResult();
        }
        catch(IndexerException | RuntimeException e)
        {
            this.indexerQueryContextFactory.destroyContext(e);
            throw e;
        }
    }
}
