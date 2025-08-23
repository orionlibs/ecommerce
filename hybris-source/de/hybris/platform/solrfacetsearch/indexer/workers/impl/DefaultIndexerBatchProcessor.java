package de.hybris.platform.solrfacetsearch.indexer.workers.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.processing.distributed.simple.SimpleBatchProcessor;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerRuntimeException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategyFactory;
import de.hybris.platform.solrfacetsearch.model.SolrIndexerDistributedProcessModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerBatchProcessor implements SimpleBatchProcessor
{
    private FacetSearchConfigService facetSearchConfigService;
    private IndexerBatchStrategyFactory indexerBatchStrategyFactory;
    private SolrSearchProviderFactory solrSearchProviderFactory;


    public void process(SimpleBatchModel inputBatch)
    {
        try
        {
            SolrIndexerDistributedProcessModel distributedProcessModel = (SolrIndexerDistributedProcessModel)inputBatch.getProcess();
            FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(distributedProcessModel.getFacetSearchConfig());
            IndexedType indexedType = this.facetSearchConfigService.resolveIndexedType(facetSearchConfig, distributedProcessModel
                            .getIndexedType());
            List<IndexedProperty> indexedProperties = this.facetSearchConfigService.resolveIndexedProperties(facetSearchConfig, indexedType, distributedProcessModel
                            .getIndexedProperties());
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, distributedProcessModel.getIndex());
            List<PK> pks = asList(inputBatch.getContext());
            IndexerBatchStrategy indexerBatchStrategy = this.indexerBatchStrategyFactory.createIndexerBatchStrategy(facetSearchConfig);
            indexerBatchStrategy.setIndexOperationId(distributedProcessModel.getIndexOperationId());
            indexerBatchStrategy
                            .setIndexOperation(
                                            IndexOperation.valueOf(distributedProcessModel.getIndexOperation().getCode().toUpperCase(Locale.ROOT)));
            indexerBatchStrategy.setExternalIndexOperation(distributedProcessModel.isExternalIndexOperation());
            indexerBatchStrategy.setFacetSearchConfig(facetSearchConfig);
            indexerBatchStrategy.setIndexedType(indexedType);
            indexerBatchStrategy.setIndexedProperties(indexedProperties);
            indexerBatchStrategy.setIndex(index);
            indexerBatchStrategy.setIndexerHints(distributedProcessModel.getIndexerHints());
            indexerBatchStrategy.setPks(pks);
            indexerBatchStrategy.execute();
        }
        catch(IndexerException | de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException | de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException e)
        {
            throw new IndexerRuntimeException(e);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }


    protected List<PK> asList(Object ctx)
    {
        Preconditions.checkState(ctx instanceof List, "ctx must be instance of List");
        return (List<PK>)ctx;
    }


    public FacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public IndexerBatchStrategyFactory getIndexerBatchStrategyFactory()
    {
        return this.indexerBatchStrategyFactory;
    }


    @Required
    public void setIndexerBatchStrategyFactory(IndexerBatchStrategyFactory indexerBatchStrategyFactory)
    {
        this.indexerBatchStrategyFactory = indexerBatchStrategyFactory;
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
