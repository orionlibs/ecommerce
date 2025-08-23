package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Objects;
import java.util.stream.Stream;

class ImportProcessCreationContext implements DistributedProcessHandler.ProcessCreationContext
{
    private final ModelService modelService;
    private final ImportProcessCreationData processData;
    private String header = null;
    private int groupId = 0;
    private int batchId = 0;


    public ImportProcessCreationContext(ModelService modelService, ImportProcessCreationData processData)
    {
        Objects.requireNonNull(modelService, "modelService mustn't be null.");
        Objects.requireNonNull(processData, "processData mustn't be null.");
        this.modelService = modelService;
        this.processData = processData;
    }


    public DistributedProcessHandler.ModelWithDependencies<DistributedProcessModel> createProcessModel()
    {
        ImpExImportCronJobModel cronJob = prepareImportCronJob(this.processData.getProcessCode());
        DistributedImportProcessModel process = (DistributedImportProcessModel)this.modelService.create(DistributedImportProcessModel.class);
        process.setCode(this.processData.getProcessCode());
        process.setHandlerBeanId(this.processData.getHandlerBeanId());
        process.setImpExImportCronJob(cronJob);
        process.setMetadata(this.processData.getImportProcessMetadata().getStringRepresentation());
        this.processData.connectToCronJob(cronJob);
        return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)process, new ItemModel[] {(ItemModel)cronJob});
    }


    private ImpExImportCronJobModel prepareImportCronJob(String processCode)
    {
        ImpExImportJobModel job = (ImpExImportJobModel)this.modelService.create(ImpExImportJobModel.class);
        job.setCode(processCode);
        ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel)this.modelService.create(ImpExImportCronJobModel.class);
        cronJob.setCode(processCode);
        cronJob.setJob((JobModel)job);
        cronJob.setStatus(CronJobStatus.PAUSED);
        cronJob.setLogLevelFile(this.processData.getEffectiveLogLevel());
        cronJob.setLogToDatabase(Boolean.FALSE);
        cronJob.setLogToFile(Boolean.TRUE);
        return cronJob;
    }


    public Stream<DistributedProcessHandler.ModelWithDependencies<BatchModel>> initialBatches()
    {
        this.header = null;
        try
        {
            return this.processData.initialBatches().sequential().map(this::createInitialBatch);
        }
        finally
        {
            this.header = null;
        }
    }


    private DistributedProcessHandler.ModelWithDependencies<BatchModel> createInitialBatch(ImportBatchCreationData batchData)
    {
        ImportBatchModel batch = (ImportBatchModel)this.modelService.create(ImportBatchModel.class);
        batch.setGroup(getGroupId(batchData));
        batch.setId(Long.toString(this.batchId++));
        batch.setRemainingWorkLoad(batchData.getRemainingWorkLoad());
        batch.setType(BatchType.INITIAL);
        ImportBatchContentModel content = (ImportBatchContentModel)this.modelService.create(ImportBatchContentModel.class);
        content.setCode(this.processData.getProcessCode() + "_" + this.processData.getProcessCode());
        content.setContent(batchData.getContent());
        batch.setImportContentCode(content.getCode());
        return DistributedProcessHandler.ModelWithDependencies.modelWithDependencies((ItemModel)batch, new ItemModel[] {(ItemModel)content});
    }


    private int getGroupId(ImportBatchCreationData batchData)
    {
        String batchHeader = batchData.getHeader();
        if(batchHeader.equals(this.header))
        {
            return this.groupId;
        }
        int result = (this.header == null) ? (this.groupId = 0) : ++this.groupId;
        this.header = batchHeader;
        return result;
    }
}
