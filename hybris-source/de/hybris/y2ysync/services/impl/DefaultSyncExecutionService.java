package de.hybris.y2ysync.services.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;
import java.util.Collection;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSyncExecutionService implements SyncExecutionService
{
    private ModelService modelService;
    private CronJobService cronJobService;
    private FlexibleSearchService flexibleSearchService;


    public Y2YSyncCronJobModel startSync(String jobCode, SyncExecutionService.ExecutionMode executionMode)
    {
        return startSync(findSyncJobByCode(jobCode), executionMode);
    }


    public Y2YSyncCronJobModel startSync(Y2YSyncJobModel job, SyncExecutionService.ExecutionMode executionMode)
    {
        Objects.requireNonNull(job, "job is required");
        Objects.requireNonNull(executionMode, "executionMode is required");
        CronJobFactory<Y2YSyncCronJobModel, Y2YSyncJobModel> cronJobFactory = this.cronJobService.getCronJobFactory((ServicelayerJobModel)job);
        CronJobModel cronJob = cronJobFactory.createCronJob((JobModel)job);
        this.cronJobService.performCronJob(cronJob, (executionMode == SyncExecutionService.ExecutionMode.SYNC));
        return (Y2YSyncCronJobModel)cronJob;
    }


    private Y2YSyncJobModel findSyncJobByCode(String jobCode)
    {
        Objects.requireNonNull(jobCode, "jobCode is required");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YSyncJob} WHERE {code}=?code");
        fQuery.addQueryParameter("code", jobCode);
        return (Y2YSyncJobModel)this.flexibleSearchService.searchUnique(fQuery);
    }


    public Collection<Y2YSyncJobModel> getAllSyncJobs()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YSyncJob}");
        SearchResult<Y2YSyncJobModel> result = this.flexibleSearchService.search(fQuery);
        return result.getResult();
    }


    public Collection<Y2YSyncJobModel> getSyncJobsForContainer(Y2YStreamConfigurationContainerModel container)
    {
        Objects.requireNonNull(container, "container is required to execute query");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YSyncJob} WHERE {streamConfigurationContainer}=?container");
        fQuery.addQueryParameter("container", container);
        SearchResult<Y2YSyncJobModel> result = this.flexibleSearchService.search(fQuery);
        return result.getResult();
    }


    public Y2YSyncJobModel createSyncJobForDataHub(String code, Y2YStreamConfigurationContainerModel container)
    {
        return createSyncJobForDataHub(code, container, null);
    }


    public Y2YSyncJobModel createSyncJobForDataHub(String code, Y2YStreamConfigurationContainerModel container, String dataHubUrl)
    {
        return createSyncJob(code, Y2YSyncType.DATAHUB, container, dataHubUrl);
    }


    public Y2YSyncJobModel createSyncJobForZip(String code, Y2YStreamConfigurationContainerModel container)
    {
        return createSyncJob(code, Y2YSyncType.ZIP, container, null);
    }


    private Y2YSyncJobModel createSyncJob(String code, Y2YSyncType syncType, Y2YStreamConfigurationContainerModel container, String dataHubUrl)
    {
        Objects.requireNonNull(code, "code is required");
        Objects.requireNonNull(syncType, "syncType is required");
        Objects.requireNonNull(container, "container is required");
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)this.modelService.create(Y2YSyncJobModel.class);
        syncJob.setCode(code);
        syncJob.setSyncType(syncType);
        syncJob.setStreamConfigurationContainer(container);
        syncJob.setDataHubUrl(dataHubUrl);
        return syncJob;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
