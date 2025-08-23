package de.hybris.y2ysync.task.distributed;

import com.google.common.base.Preconditions;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YBatchModel;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Y2YSyncDistributedProcessHandler implements DistributedProcessHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(Y2YSyncDistributedProcessHandler.class);
    static final long REMAINING_WORKLOAD = 1L;
    static final long NO_MORE_WORK_WEIGHT = 0L;
    private final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final MediaService mediaService;
    private final TypeService typeService;
    private final ChangeDetectionService changeDetectionService;
    private final BatchingExportProcessor batchingExportProcessor;
    private final FinalizeProcessHandler finalizeProcessHandler;
    private final DistributedProcessService distributedProcessService;


    public Y2YSyncDistributedProcessHandler(ModelService modelService, FlexibleSearchService flexibleSearchService, MediaService mediaService, TypeService typeService, ChangeDetectionService changeDetectionService, DistributedProcessService distributedProcessService,
                    BatchingExportProcessor batchingExportProcessor, FinalizeProcessHandler finalizeProcessHandler)
    {
        this.modelService = modelService;
        this.flexibleSearchService = flexibleSearchService;
        this.mediaService = mediaService;
        this.typeService = typeService;
        this.changeDetectionService = changeDetectionService;
        this.distributedProcessService = distributedProcessService;
        this.batchingExportProcessor = batchingExportProcessor;
        this.finalizeProcessHandler = finalizeProcessHandler;
    }


    public Y2YSyncProcessCreationContext createProcessCreationContext(ProcessCreationData data)
    {
        Y2YSyncProcessCreationData creationData = assureInstanceOf(data, Y2YSyncProcessCreationData.class);
        return new Y2YSyncProcessCreationContext(creationData, this.modelService);
    }


    public Y2YSyncProcessInitializationContext createProcessInitializationContext(DistributedProcessModel process)
    {
        Y2YDistributedProcessModel _process = assureInstanceOf(process, Y2YDistributedProcessModel.class);
        return new Y2YSyncProcessInitializationContext(_process, this.flexibleSearchService, this.modelService, this.mediaService, this.typeService, this.changeDetectionService);
    }


    public DistributedProcessHandler.ModelWithDependencies<BatchModel> createResultBatch(BatchModel inputBatch)
    {
        Y2YBatchModel _inputBatch = assureInstanceOf(inputBatch, Y2YBatchModel.class);
        if(_inputBatch.isFinalize())
        {
            return finalizeProcess(_inputBatch);
        }
        return getResultBatch(_inputBatch);
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> finalizeProcess(Y2YBatchModel _inputBatch)
    {
        try
        {
            Y2YDistributedProcessModel process = assureInstanceOf(_inputBatch.getProcess(), Y2YDistributedProcessModel.class);
            Y2YSyncCronJobModel resultingCronJob = this.finalizeProcessHandler.finalizeFor(process);
            return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)createFinalResultingBatch(), new ItemModel[] {(ItemModel)resultingCronJob});
        }
        catch(Exception e)
        {
            LOG.warn("There was an error when trying to finalize Y2Y Sync process (reason: {})", e.getMessage());
            Y2YBatchModel resultBatch = createErrorResultBatch(_inputBatch);
            resultBatch.setFinalize(true);
            return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)resultBatch);
        }
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> getResultBatch(Y2YBatchModel _inputBatch)
    {
        try
        {
            BatchModel resultBatch = this.batchingExportProcessor.getResultBatch(_inputBatch);
            return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)resultBatch);
        }
        catch(Exception e)
        {
            Y2YBatchModel resultBatch = createErrorResultBatch(_inputBatch);
            LOG.error("There was an error when trying to export data (reason: {})", e.getMessage());
            return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)resultBatch);
        }
    }


    private Y2YBatchModel createFinalResultingBatch()
    {
        Y2YBatchModel batch = (Y2YBatchModel)this.modelService.create(Y2YBatchModel.class);
        batch.setRemainingWorkLoad(0L);
        batch.setType(BatchType.RESULT);
        return batch;
    }


    private Y2YBatchModel createErrorResultBatch(Y2YBatchModel inputBatch)
    {
        Y2YBatchModel batch = (Y2YBatchModel)this.modelService.create(Y2YBatchModel.class);
        batch.setRemainingWorkLoad(1L);
        batch.setRetries(inputBatch.getRetries());
        batch.setError(true);
        batch.setType(BatchType.RESULT);
        return batch;
    }


    public Y2YSyncProcessAnalysisContext createProcessExecutionAnalysisContext(DistributedProcessModel process)
    {
        Y2YDistributedProcessModel _process = assureInstanceOf(process, Y2YDistributedProcessModel.class);
        return new Y2YSyncProcessAnalysisContext(_process, this.flexibleSearchService, this.modelService, this.distributedProcessService);
    }


    public void onFinished(DistributedProcessModel process)
    {
        LOG.info("##### Y2YSync Process {} has finished", process.getCode());
    }


    private <T> T assureInstanceOf(Object object, Class<T> expctedClass)
    {
        Objects.requireNonNull(object, "input object must not be null");
        Preconditions.checkArgument(expctedClass.isInstance(object), "object %s is not instance of %s", object, expctedClass);
        return (T)object;
    }
}
