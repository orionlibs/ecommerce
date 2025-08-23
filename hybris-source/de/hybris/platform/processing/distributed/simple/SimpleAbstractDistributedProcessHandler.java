package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.distributed.defaultimpl.RemainingWorkloadBasedProgressCalculator;
import de.hybris.platform.processing.distributed.simple.context.SimpleDistributedProcessExecutionAnalysisContext;
import de.hybris.platform.processing.distributed.simple.context.SimpleDistributedProcessInitializationContext;
import de.hybris.platform.processing.distributed.simple.context.SimpleProcessCreationContext;
import de.hybris.platform.processing.distributed.simple.data.SimpleAbstractDistributedProcessCreationData;
import de.hybris.platform.processing.distributed.simple.id.SimpleBatchID;
import de.hybris.platform.processing.distributed.simple.util.DistributedProcessUtils;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Objects;
import java.util.OptionalDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleAbstractDistributedProcessHandler implements DistributedProcessHandler
{
    protected static final Logger LOG = LoggerFactory.getLogger(SimpleDistributedProcessHandler.class);
    public static final long REMAINING_WORKLOAD = 10L;
    public static final long WORK_DONE = 0L;
    protected final ModelService modelService;
    protected final FlexibleSearchService flexibleSearchService;
    protected final DistributedProcessService distributedProcessService;


    protected SimpleAbstractDistributedProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, DistributedProcessService distributedProcessService)
    {
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService is required");
        this.flexibleSearchService = Objects.<FlexibleSearchService>requireNonNull(flexibleSearchService, "flexibleSearchService is required");
        this.distributedProcessService = Objects.<DistributedProcessService>requireNonNull(distributedProcessService, "distributedProcessService is required");
    }


    public SimpleProcessCreationContext createProcessCreationContext(ProcessCreationData processData)
    {
        SimpleAbstractDistributedProcessCreationData _processData = (SimpleAbstractDistributedProcessCreationData)DistributedProcessUtils.assureInstanceOf(processData, SimpleAbstractDistributedProcessCreationData.class);
        return prepareProcessCreationContext(_processData);
    }


    protected SimpleProcessCreationContext prepareProcessCreationContext(SimpleAbstractDistributedProcessCreationData processData)
    {
        return new SimpleProcessCreationContext(this.modelService, processData);
    }


    public DistributedProcessHandler.ProcessInitializationContext createProcessInitializationContext(DistributedProcessModel process)
    {
        SimpleDistributedProcessModel _process = (SimpleDistributedProcessModel)DistributedProcessUtils.assureInstanceOf(process, SimpleDistributedProcessModel.class);
        return (DistributedProcessHandler.ProcessInitializationContext)new SimpleDistributedProcessInitializationContext(this.flexibleSearchService, this.modelService, _process);
    }


    public DistributedProcessHandler.ModelWithDependencies<BatchModel> createResultBatch(BatchModel inputBatch)
    {
        SimpleBatchModel _inputBatch = (SimpleBatchModel)DistributedProcessUtils.assureInstanceOf(inputBatch, SimpleBatchModel.class);
        processBatch(_inputBatch);
        BatchModel resultBatch = prepareResultBatch();
        _inputBatch.setResultBatchId(resultBatch.getId());
        return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)resultBatch, new ItemModel[] {(ItemModel)_inputBatch});
    }


    protected BatchModel prepareResultBatch()
    {
        SimpleBatchModel resultBatch = (SimpleBatchModel)this.modelService.create(SimpleBatchModel.class);
        resultBatch.setId(SimpleBatchID.asResultBatchID().toString());
        resultBatch.setType(BatchType.RESULT);
        resultBatch.setRemainingWorkLoad(0L);
        return (BatchModel)resultBatch;
    }


    public DistributedProcessHandler.ProcessExecutionAnalysisContext createProcessExecutionAnalysisContext(DistributedProcessModel process)
    {
        SimpleDistributedProcessModel _process = (SimpleDistributedProcessModel)DistributedProcessUtils.assureInstanceOf(process, SimpleDistributedProcessModel.class);
        return (DistributedProcessHandler.ProcessExecutionAnalysisContext)new SimpleDistributedProcessExecutionAnalysisContext(this.flexibleSearchService, this.modelService, this.distributedProcessService, (DistributedProcessModel)_process);
    }


    public void onFinished(DistributedProcessModel process)
    {
        LOG.info("#### Process {} has finished", process.getCode());
    }


    public OptionalDouble calculateProgress(DistributedProcessModel process)
    {
        double progress = (new RemainingWorkloadBasedProgressCalculator(process, this.flexibleSearchService)).calculateProgress();
        return OptionalDouble.of(progress);
    }


    public abstract void processBatch(SimpleBatchModel paramSimpleBatchModel);
}
