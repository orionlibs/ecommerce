package de.hybris.y2ysync.task.distributed;

import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.model.Y2YBatchModel;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import java.util.Objects;
import java.util.stream.Stream;

public class Y2YSyncProcessCreationContext implements DistributedProcessHandler.ProcessCreationContext
{
    private final Y2YSyncProcessCreationData creationData;
    private final ModelService modelService;
    private int batchId = 0;


    public Y2YSyncProcessCreationContext(Y2YSyncProcessCreationData processCreationData, ModelService modelService)
    {
        this.creationData = Objects.<Y2YSyncProcessCreationData>requireNonNull(processCreationData, "processCreationData is required");
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService is required");
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> createProcessModel()
    {
        Y2YSyncCronJobModel syncCronJob = createSyncCronJob();
        DistributedProcessModel process = createDistributedProcessModel(syncCronJob);
        return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)process, new ItemModel[] {(ItemModel)syncCronJob});
    }


    private DistributedProcessModel createDistributedProcessModel(Y2YSyncCronJobModel syncCronJob)
    {
        Y2YDistributedProcessModel process = (Y2YDistributedProcessModel)this.modelService.create(Y2YDistributedProcessModel.class);
        process.setCode(this.creationData.getProcessId());
        process.setHandlerBeanId(this.creationData.getHandlerBeanId());
        process.setBatchSize(this.creationData.getBatchSize());
        process.setY2ySyncCronJob(syncCronJob);
        return (DistributedProcessModel)process;
    }


    private Y2YSyncCronJobModel createSyncCronJob()
    {
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)this.modelService.create(Y2YSyncJobModel.class);
        syncJob.setCode(this.creationData.getProcessId());
        syncJob.setSyncType(this.creationData.getSyncType());
        syncJob.setStreamConfigurationContainer(this.creationData.getContainer());
        syncJob.setDataHubUrl(this.creationData.getDataHubUrl());
        this.modelService.save(syncJob);
        Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel)this.modelService.create(Y2YSyncCronJobModel.class);
        cronJob.setCode(this.creationData.getProcessId());
        cronJob.setJob((JobModel)syncJob);
        cronJob.setStatus(CronJobStatus.RUNNING);
        this.modelService.save(cronJob);
        return cronJob;
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> initialBatches()
    {
        return this.creationData.initialBatches().sequential().map(this::createInitialBatch);
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> createInitialBatch(StreamConfiguration streamConfiguration)
    {
        Y2YBatchModel batch = (Y2YBatchModel)this.modelService.create(Y2YBatchModel.class);
        batch.setId(Long.toString(this.batchId++));
        batch.setRemainingWorkLoad(1L);
        batch.setType(BatchType.INITIAL);
        batch.setContext(streamConfiguration);
        return DistributedProcessHandler.ModelWithDependencies.singleModel((ItemModel)batch);
    }
}
