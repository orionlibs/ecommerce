package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.context.SimpleProcessCreationContext;
import de.hybris.platform.processing.distributed.simple.data.SimpleAbstractDistributedProcessCreationData;
import de.hybris.platform.processing.distributed.simple.data.SimpleBatchCreationData;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.model.SolrIndexerBatchModel;

public class DefaultIndexerProcessCreationContext extends SimpleProcessCreationContext
{
    public DefaultIndexerProcessCreationContext(ModelService modelService, SimpleAbstractDistributedProcessCreationData creationData)
    {
        super(modelService, creationData);
    }


    protected DistributedProcessHandler.ModelWithDependencies<BatchModel> prepareBatch(SimpleBatchCreationData data)
    {
        SolrIndexerBatchModel initialBatch = (SolrIndexerBatchModel)this.modelService.create(SolrIndexerBatchModel.class);
        initialBatch.setId(SimpleBatchID.asInitialBatch().toString());
        initialBatch.setType(BatchType.INITIAL);
        initialBatch.setContext(data.getContext());
        initialBatch.setRemainingWorkLoad(10L);
        initialBatch.setRetries(this.creationData.getNumOfRetries());
        initialBatch.setScriptCode(this.creationData.getScriptCode());
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)initialBatch);
    }
}
