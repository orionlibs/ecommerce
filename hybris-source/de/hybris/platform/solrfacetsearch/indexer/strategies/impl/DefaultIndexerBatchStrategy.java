package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.solr.Index;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerBatchStrategy implements IndexerBatchStrategy
{
    private IndexerQueriesExecutor indexerQueriesExecutor;
    private IndexerBatchContextFactory<?> indexerBatchContextFactory;
    private Indexer indexer;
    private long indexOperationId;
    private IndexOperation indexOperation;
    private boolean externalIndexOperation;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;
    private Collection<IndexedProperty> indexedProperties;
    private Index index;
    private Map<String, String> indexerHints;
    private List<PK> pks;


    public Indexer getIndexer()
    {
        return this.indexer;
    }


    @Required
    public void setIndexer(Indexer indexer)
    {
        this.indexer = indexer;
    }


    public IndexerBatchContextFactory getIndexerBatchContextFactory()
    {
        return this.indexerBatchContextFactory;
    }


    @Required
    public void setIndexerBatchContextFactory(IndexerBatchContextFactory<?> indexerBatchContextFactory)
    {
        this.indexerBatchContextFactory = indexerBatchContextFactory;
    }


    public IndexerQueriesExecutor getIndexerQueriesExecutor()
    {
        return this.indexerQueriesExecutor;
    }


    @Required
    public void setIndexerQueriesExecutor(IndexerQueriesExecutor indexerQueriesExecutor)
    {
        this.indexerQueriesExecutor = indexerQueriesExecutor;
    }


    public boolean isExternalIndexOperation()
    {
        return this.externalIndexOperation;
    }


    public void setExternalIndexOperation(boolean externalIndexOperation)
    {
        this.externalIndexOperation = externalIndexOperation;
    }


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setFacetSearchConfig(FacetSearchConfig facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public Index getIndex()
    {
        return this.index;
    }


    public void setIndex(Index index)
    {
        this.index = index;
    }


    public Collection<IndexedProperty> getIndexedProperties()
    {
        return this.indexedProperties;
    }


    public void setIndexedProperties(Collection<IndexedProperty> indexedProperties)
    {
        this.indexedProperties = indexedProperties;
    }


    public IndexedType getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedType(IndexedType indexedType)
    {
        this.indexedType = indexedType;
    }


    public Map<String, String> getIndexerHints()
    {
        return this.indexerHints;
    }


    public void setIndexerHints(Map<String, String> indexerHints)
    {
        this.indexerHints = indexerHints;
    }


    public IndexOperation getIndexOperation()
    {
        return this.indexOperation;
    }


    public void setIndexOperation(IndexOperation indexOperation)
    {
        this.indexOperation = indexOperation;
    }


    public long getIndexOperationId()
    {
        return this.indexOperationId;
    }


    public void setIndexOperationId(long indexOperationId)
    {
        this.indexOperationId = indexOperationId;
    }


    public List<PK> getPks()
    {
        return this.pks;
    }


    public void setPks(List<PK> pks)
    {
        this.pks = pks;
    }


    public void execute() throws InterruptedException, IndexerException
    {
        validateRequiredFields();
        try
        {
            IndexerBatchContext batchContext = this.indexerBatchContextFactory.createContext(this.indexOperationId, this.indexOperation, this.externalIndexOperation, this.facetSearchConfig, this.indexedType, this.indexedProperties);
            batchContext.getIndexerHints().putAll(this.indexerHints);
            batchContext.setIndex(this.index);
            batchContext.setPks(this.pks);
            this.indexerBatchContextFactory.prepareContext();
            if(batchContext.getIndexOperation() == IndexOperation.DELETE)
            {
                batchContext.setItems(Collections.emptyList());
            }
            else
            {
                List<ItemModel> items = executeIndexerQuery(this.facetSearchConfig, this.indexedType, this.pks);
                batchContext.setItems(items);
            }
            this.indexerBatchContextFactory.initializeContext();
            executeIndexerOperation(batchContext);
            this.indexerBatchContextFactory.destroyContext();
        }
        catch(IndexerException | InterruptedException | RuntimeException e)
        {
            this.indexerBatchContextFactory.destroyContext(e);
            throw e;
        }
    }


    protected void validateRequiredFields()
    {
        ServicesUtil.validateParameterNotNull(this.indexOperation, "indexOperation field not set");
        ServicesUtil.validateParameterNotNull(this.facetSearchConfig, "facetSearchConfig field not set");
        ServicesUtil.validateParameterNotNull(this.indexedType, "indexedType field not set");
        ServicesUtil.validateParameterNotNull(this.indexedProperties, "indexedProperties field not set");
        ServicesUtil.validateParameterNotNull(this.index, "index field not set");
        ServicesUtil.validateParameterNotNull(this.indexerHints, "indexerHints field not set");
        ServicesUtil.validateParameterNotNull(this.pks, "pks field not set");
        ServicesUtil.validateIfAnyResult(this.pks, "pks field not set");
    }


    protected List<ItemModel> executeIndexerQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks) throws IndexerException
    {
        return this.indexerQueriesExecutor.getItems(facetSearchConfig, indexedType, pks);
    }


    protected void executeIndexerOperation(IndexerBatchContext batchContext) throws IndexerException, InterruptedException
    {
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$config$IndexOperation[batchContext.getIndexOperation().ordinal()])
        {
            case 1:
            case 2:
                this.indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType());
                return;
            case 3:
                this.indexer.indexItems(batchContext.getItems(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext
                                .getIndexedProperties());
                return;
            case 4:
                this.indexer.removeItemsByPk(batchContext.getPks(), batchContext.getFacetSearchConfig(), batchContext.getIndexedType(), batchContext
                                .getIndex());
                return;
        }
        throw new IndexerException("Unsupported index operation: " + batchContext.getIndexOperation());
    }
}
