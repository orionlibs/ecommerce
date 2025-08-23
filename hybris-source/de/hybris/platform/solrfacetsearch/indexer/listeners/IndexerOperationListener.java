package de.hybris.platform.solrfacetsearch.indexer.listeners;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.enums.IndexMode;
import de.hybris.platform.solrfacetsearch.indexer.ExtendedIndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexOperationService;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import org.springframework.beans.factory.annotation.Required;

public class IndexerOperationListener implements ExtendedIndexerListener
{
    public static final String DEFAULT_QUALIFIER = "default";
    public static final String FLIP_QUALIFIER = "flip";
    public static final String FLOP_QUALIFIER = "flop";
    private SolrIndexService solrIndexService;
    private SolrIndexOperationService solrIndexOperationService;
    private SolrSearchProviderFactory solrSearchProviderFactory;


    public void afterPrepareContext(IndexerContext context) throws IndexerException
    {
        try
        {
            FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
            IndexedType indexedType = context.getIndexedType();
            IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            IndexOperation indexOperation = context.getIndexOperation();
            if(indexOperation == IndexOperation.FULL)
            {
                String newQualifier = "default";
                if(indexConfig.getIndexMode() == IndexMode.TWO_PHASE)
                {
                    newQualifier = resolveStagedQualifier(context.getIndex());
                }
                Index newIndex = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, newQualifier);
                context.setIndex(newIndex);
            }
            Index index = context.getIndex();
            SolrIndexModel solrIndex = this.solrIndexService.getOrCreateIndex(facetSearchConfig.getName(), indexedType
                            .getIdentifier(), index.getQualifier());
            this.solrIndexOperationService.startOperation(solrIndex, context.getIndexOperationId(), indexOperation, context
                            .isExternalIndexOperation());
            if(indexOperation == IndexOperation.FULL)
            {
                solrSearchProvider.createIndex(context.getIndex());
                solrSearchProvider.exportConfig(context.getIndex());
                if(indexConfig.getIndexMode() == IndexMode.TWO_PHASE)
                {
                    solrSearchProvider.deleteAllDocuments(index);
                }
            }
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    public void beforeIndex(IndexerContext context) throws IndexerException
    {
    }


    public void afterIndex(IndexerContext context) throws IndexerException
    {
        try
        {
            FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
            IndexedType indexedType = context.getIndexedType();
            IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
            Index index = context.getIndex();
            IndexOperation indexOperation = context.getIndexOperation();
            if(indexOperation == IndexOperation.FULL)
            {
                if(indexConfig.getIndexMode() == IndexMode.DIRECT)
                {
                    SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
                    solrSearchProvider.deleteOldDocuments(index, context.getIndexOperationId());
                }
                this.solrIndexService.activateIndex(facetSearchConfig.getName(), indexedType.getIdentifier(), index.getQualifier());
            }
            this.solrIndexOperationService.endOperation(context.getIndexOperationId(), false);
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    public void afterIndexError(IndexerContext context) throws IndexerException
    {
        try
        {
            this.solrIndexOperationService.endOperation(context.getIndexOperationId(), true);
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    protected String resolveStagedQualifier(Index index)
    {
        if(index != null && "flip".equals(index.getQualifier()))
        {
            return "flop";
        }
        return "flip";
    }


    public SolrIndexOperationService getSolrIndexOperationService()
    {
        return this.solrIndexOperationService;
    }


    @Required
    public void setSolrIndexOperationService(SolrIndexOperationService solrIndexOperationService)
    {
        this.solrIndexOperationService = solrIndexOperationService;
    }


    public SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }
}
