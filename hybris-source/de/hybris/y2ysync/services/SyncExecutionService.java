package de.hybris.y2ysync.services;

import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import java.util.Collection;

public interface SyncExecutionService
{
    Y2YSyncCronJobModel startSync(String paramString, ExecutionMode paramExecutionMode);


    Y2YSyncCronJobModel startSync(Y2YSyncJobModel paramY2YSyncJobModel, ExecutionMode paramExecutionMode);


    Collection<Y2YSyncJobModel> getAllSyncJobs();


    Collection<Y2YSyncJobModel> getSyncJobsForContainer(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel);


    Y2YSyncJobModel createSyncJobForDataHub(String paramString, Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel);


    Y2YSyncJobModel createSyncJobForDataHub(String paramString1, Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString2);


    Y2YSyncJobModel createSyncJobForZip(String paramString, Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel);
}
