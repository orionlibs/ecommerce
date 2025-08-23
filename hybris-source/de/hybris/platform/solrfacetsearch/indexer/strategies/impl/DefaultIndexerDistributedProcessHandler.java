package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.simple.SimpleBatchProcessor;
import de.hybris.platform.processing.distributed.simple.SimpleDistributedProcessHandler;
import de.hybris.platform.processing.distributed.simple.context.SimpleProcessCreationContext;
import de.hybris.platform.processing.distributed.simple.data.SimpleAbstractDistributedProcessCreationData;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.solrfacetsearch.model.SolrIndexerBatchModel;

public class DefaultIndexerDistributedProcessHandler extends SimpleDistributedProcessHandler
{
    public DefaultIndexerDistributedProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, DistributedProcessService distributedProcessService, SimpleBatchProcessor simpleBatchProcessor)
    {
        super(modelService, flexibleSearchService, distributedProcessService, simpleBatchProcessor);
    }


    protected SimpleProcessCreationContext prepareProcessCreationContext(SimpleAbstractDistributedProcessCreationData processData)
    {
        return (SimpleProcessCreationContext)new DefaultIndexerProcessCreationContext(this.modelService, processData);
    }


    protected BatchModel prepareResultBatch()
    {
        SolrIndexerBatchModel resultBatch = (SolrIndexerBatchModel)this.modelService.create(SolrIndexerBatchModel.class);
        resultBatch.setId(SimpleBatchID.asResultBatchID().toString());
        resultBatch.setType(BatchType.RESULT);
        resultBatch.setRemainingWorkLoad(0L);
        return (BatchModel)resultBatch;
    }
}
