package com.hybris.backoffice.solrsearch.setup;

import com.hybris.backoffice.search.setup.AbstractBackofficeSearchIndexInitializer;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeSolrSearchIndexInitializer extends AbstractBackofficeSearchIndexInitializer
{
    private SolrIndexService solrIndexService;
    private IndexerService indexerService;
    private static final String PROPERTY_INDEX_AUTOINIT = "backoffice.solr.search.index.autoinit";
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSolrSearchIndexInitializer.class);


    protected void initializeIndexesIfNecessary()
    {
        if(shouldInitializeIndexes())
        {
            this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs().stream()
                            .filter(searchConfig -> !isIndexInitialized((FacetSearchConfig)searchConfig)).forEach(this::initializeIndex);
        }
        else
        {
            LOG.info("Backoffice SOLR indices initialization disabled by property");
        }
    }


    protected boolean shouldInitializeIndexes()
    {
        return Config.getBoolean("backoffice.solr.search.index.autoinit", true);
    }


    protected boolean isIndexInitialized(FacetSearchConfig searchConfig)
    {
        try
        {
            IndexedType indexedType = searchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
            getSolrIndexService().getActiveIndex(searchConfig.getName(), indexedType.getIdentifier());
            return true;
        }
        catch(SolrServiceException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.info("Index for '{}' configuration not initialized", searchConfig.getName(), e);
            }
            else
            {
                LOG.info("Index for '{}' configuration not initialized", searchConfig.getName());
            }
        }
        catch(RuntimeException re)
        {
            LOG.error("Failed to check if index is initialized", re);
        }
        return false;
    }


    protected void initializeIndex(Object searchConfig)
    {
        FacetSearchConfig facetSearchConfig = (FacetSearchConfig)searchConfig;
        LOG.info("Performing FULL INDEX operation for '{}' configuration", facetSearchConfig.getName());
        try
        {
            getIndexerService().performFullIndex(facetSearchConfig);
        }
        catch(IndexerException e)
        {
            LOG.error("Indexer error", (Throwable)e);
        }
    }


    protected SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    protected IndexerService getIndexerService()
    {
        return this.indexerService;
    }


    @Required
    public void setIndexerService(IndexerService indexerService)
    {
        this.indexerService = indexerService;
    }
}
