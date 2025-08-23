package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class SimpleDistributedProcessHandler extends SimpleAbstractDistributedProcessHandler
{
    protected final SimpleBatchProcessor simpleBatchProcessor;


    public SimpleDistributedProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, DistributedProcessService distributedProcessService, SimpleBatchProcessor simpleBatchProcessor)
    {
        super(modelService, flexibleSearchService, distributedProcessService);
        this.simpleBatchProcessor = simpleBatchProcessor;
    }


    public void processBatch(SimpleBatchModel inputBatch)
    {
        this.simpleBatchProcessor.process(inputBatch);
    }
}
