package com.hybris.backoffice.solrsearch.events;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DirectSolrIndexSynchronizationStrategy implements SolrIndexSynchronizationStrategy
{
    protected static final Logger LOG = LoggerFactory.getLogger(DirectSolrIndexSynchronizationStrategy.class);
    protected IndexerService indexerService;
    protected ModelService modelService;
    protected TypeService typeService;
    protected FacetSearchConfigService facetSearchConfigService;
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    protected SolrIndexService solrIndexService;


    public void removeItem(String typecode, long pk)
    {
        removeItems(typecode, Collections.singletonList(PK.fromLong(pk)));
    }


    public void removeItems(String typecode, List<PK> pkList)
    {
        try
        {
            SolrFacetSearchConfigModel searchConfig = (SolrFacetSearchConfigModel)this.backofficeFacetSearchConfigService.getFacetSearchConfigModel(typecode);
            if(searchConfig != null)
            {
                performIndexDelete(typecode, searchConfig, (List<String>)pkList.stream().map(PK::toString).collect(Collectors.toList()));
            }
        }
        catch(Exception e)
        {
            LOG.warn("Solr facet search config cannot be found for type: " + typecode, e);
        }
    }


    public void updateItem(String typecode, long pk)
    {
        updateItems(typecode, Collections.singletonList(PK.fromLong(pk)));
    }


    public void updateItems(String typecode, List<PK> pkList)
    {
        try
        {
            SolrFacetSearchConfigModel searchConfig = (SolrFacetSearchConfigModel)this.backofficeFacetSearchConfigService.getFacetSearchConfigModel(typecode);
            if(searchConfig != null)
            {
                performIndexUpdate(typecode, searchConfig, (List<String>)pkList.stream().map(PK::toString).collect(Collectors.toList()));
            }
        }
        catch(Exception e)
        {
            LOG.warn("Solr facet search config cannot be found for type: " + typecode, e);
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void performIndexDelete(String typecode, SolrFacetSearchConfigModel solrFacetSearchConfig, List<String> pks)
    {
        performIndexDelete(solrFacetSearchConfig, (List<PK>)pks.stream().map(PK::parse).collect(Collectors.toList()));
    }


    protected void performIndexDelete(SolrFacetSearchConfigModel solrFacetSearchConfig, List<PK> pks)
    {
        FacetSearchConfig facetSearchConfig = findFacetSearchConfig(solrFacetSearchConfig);
        if(facetSearchConfig != null && isIndexInitialized(facetSearchConfig))
        {
            Collection<IndexedType> indexedTypes = facetSearchConfig.getIndexConfig().getIndexedTypes().values();
            for(IndexedType type : indexedTypes)
            {
                try
                {
                    this.indexerService.deleteTypeIndex(facetSearchConfig, type, pks);
                }
                catch(IndexerException e)
                {
                    LOG.warn("Error during indexer call: " + facetSearchConfig.getName(), (Throwable)e);
                }
            }
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void performIndexUpdate(String typecode, SolrFacetSearchConfigModel solrFacetSearchConfig, List<String> pks)
    {
        performIndexUpdate(solrFacetSearchConfig, (List<PK>)pks.stream().map(PK::parse).collect(Collectors.toList()));
    }


    protected void performIndexUpdate(SolrFacetSearchConfigModel solrFacetSearchConfig, List<PK> pks)
    {
        FacetSearchConfig facetSearchConfig = findFacetSearchConfig(solrFacetSearchConfig);
        if(facetSearchConfig != null && isIndexInitialized(facetSearchConfig))
        {
            Collection<IndexedType> indexedTypes = facetSearchConfig.getIndexConfig().getIndexedTypes().values();
            for(IndexedType type : indexedTypes)
            {
                try
                {
                    this.indexerService.updateTypeIndex(facetSearchConfig, type, pks);
                }
                catch(IndexerException e)
                {
                    LOG.warn("Error during indexer call: " + facetSearchConfig.getName(), (Throwable)e);
                }
            }
        }
    }


    protected FacetSearchConfig findFacetSearchConfig(SolrFacetSearchConfigModel facetSearchConfigModel)
    {
        try
        {
            return this.facetSearchConfigService.getConfiguration(facetSearchConfigModel.getName());
        }
        catch(FacetConfigServiceException e)
        {
            LOG.warn("Configuration service could not load configuration with name: " + facetSearchConfigModel.getName(), (Throwable)e);
            return null;
        }
    }


    @Required
    public void setIndexerService(IndexerService indexerService)
    {
        this.indexerService = indexerService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    protected boolean isIndexInitialized(FacetSearchConfig searchConfig)
    {
        try
        {
            IndexedType indexedType = searchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
            this.solrIndexService.getActiveIndex(searchConfig.getName(), indexedType.getIdentifier());
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
}
