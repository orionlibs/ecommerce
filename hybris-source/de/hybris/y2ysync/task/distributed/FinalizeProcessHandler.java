package de.hybris.y2ysync.task.distributed;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import de.hybris.y2ysync.task.runner.internal.DataHubRequestCreator;
import de.hybris.y2ysync.task.runner.internal.ImportPackage;
import de.hybris.y2ysync.task.runner.internal.ImportZipCreator;
import java.util.Objects;

public class FinalizeProcessHandler
{
    private final ImportZipCreator importZipCreator;
    private final DataHubRequestCreator dataHubRequestCreator;


    public FinalizeProcessHandler(ImportZipCreator importZipCreator, DataHubRequestCreator dataHubRequestCreator)
    {
        this.importZipCreator = importZipCreator;
        this.dataHubRequestCreator = dataHubRequestCreator;
    }


    public Y2YSyncCronJobModel finalizeFor(Y2YDistributedProcessModel process)
    {
        Objects.requireNonNull(process, "process is required");
        Y2YSyncCronJobModel cronJob = process.getY2ySyncCronJob();
        Objects.requireNonNull(cronJob, "main cronJob for Y2YSync process not found!");
        Y2YSyncType syncType = cronJob.getJob().getSyncType();
        if(syncType == Y2YSyncType.DATAHUB)
        {
            this.dataHubRequestCreator.sendRequest(createSyncContext(process.getCode(), cronJob));
            updateSyncCronJobStatusForDataHub(cronJob);
            return cronJob;
        }
        if(syncType == Y2YSyncType.ZIP)
        {
            ImportPackage importMedias = this.importZipCreator.createImportMedias(process.getCode());
            updateSyncCronJobStatusForZip(importMedias, cronJob);
            return cronJob;
        }
        throw new IllegalStateException("No handler for syncType " + syncType);
    }


    private Y2YSyncContext createSyncContext(String syncExecutionId, Y2YSyncCronJobModel cronJob)
    {
        Y2YStreamConfigurationContainerModel container = cronJob.getJob().getStreamConfigurationContainer();
        return Y2YSyncContext.builder()
                        .withSyncExecutionId(syncExecutionId)
                        .withFeed(container.getFeed())
                        .withPool(container.getPool())
                        .withAutoPublishTargetSystems(container.getTargetSystem())
                        .withUri(cronJob.getJob().getDataHubUrl())
                        .build();
    }


    private void updateSyncCronJobStatusForZip(ImportPackage importPackage, Y2YSyncCronJobModel cronJob)
    {
        cronJob.setImpexZip(importPackage.getMediaData());
        cronJob.setMediasZip(importPackage.getMediaBinaries());
        cronJob.setStatus(CronJobStatus.FINISHED);
    }


    private void updateSyncCronJobStatusForDataHub(Y2YSyncCronJobModel cronJob)
    {
        cronJob.setStatus(CronJobStatus.FINISHED);
    }
}
