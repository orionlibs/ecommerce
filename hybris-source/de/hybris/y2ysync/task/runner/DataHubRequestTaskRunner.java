package de.hybris.y2ysync.task.runner;

import com.google.common.base.Preconditions;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.platform.util.Config;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.logging.Y2YSyncLoggingCtxFactory;
import de.hybris.y2ysync.task.runner.internal.DataHubRequestCreator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DataHubRequestTaskRunner extends AbstractMainTaskRunner implements TaskRunner<TaskModel>
{
    private Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory;
    private Y2YSyncDAO y2YSyncDAO;
    private DataHubRequestCreator requestCreator;
    private static final Logger LOG = Logger.getLogger(DataHubRequestTaskRunner.class);


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        String syncExecutionId = getSyncExecutionId(task);
        Y2YSyncCronJobModel cronJob = this.y2YSyncDAO.findSyncCronJobByCode(syncExecutionId);
        try
        {
            sendRequestToDataHub(syncExecutionId, cronJob);
            updateSyncCronJobStatus(cronJob);
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            updateSyncCronJobStatusError(cronJob);
        }
    }


    public boolean isLoggingSupported()
    {
        return true;
    }


    public TaskLoggingCtx initLoggingCtx(TaskModel task)
    {
        return this.y2YSyncLoggingCtxFactory.createY2YSyncTaskLogger(task);
    }


    public void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
        this.y2YSyncLoggingCtxFactory.finish(taskCtx);
    }


    private void sendRequestToDataHub(String syncExecutionId, Y2YSyncCronJobModel cronJob)
    {
        Y2YStreamConfigurationContainerModel configContainer = cronJob.getJob().getStreamConfigurationContainer();
        Y2YSyncContext ctx = Y2YSyncContext.builder().withSyncExecutionId(syncExecutionId).withUri(getDataHubUrl(cronJob.getJob())).withFeed(configContainer.getFeed()).withPool(configContainer.getPool()).withAutoPublishTargetSystems(configContainer.getTargetSystem()).build();
        this.requestCreator.sendRequest(ctx);
    }


    private void updateSyncCronJobStatus(Y2YSyncCronJobModel cronJob)
    {
        cronJob.setStatus(CronJobStatus.FINISHED);
        this.modelService.save(cronJob);
    }


    private void updateSyncCronJobStatusError(Y2YSyncCronJobModel cronJob)
    {
        cronJob.setStatus(CronJobStatus.FINISHED);
        cronJob.setResult(CronJobResult.ERROR);
        this.modelService.save(cronJob);
    }


    private String getDataHubUrl(Y2YSyncJobModel job)
    {
        String dataHubUrl = StringUtils.isNotBlank(job.getDataHubUrl()) ? job.getDataHubUrl() : getDataHubUrlFromProperties();
        Preconditions.checkState(StringUtils.isNotBlank(dataHubUrl), "DataHub url is missing (y2ysync.datahub.url property not set)");
        return dataHubUrl;
    }


    String getDataHubUrlFromProperties()
    {
        return Config.getString("y2ysync.datahub.url", null);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    @Required
    public void setY2YSyncDAO(Y2YSyncDAO y2YSyncDAO)
    {
        this.y2YSyncDAO = y2YSyncDAO;
    }


    @Required
    public void setRequestCreator(DataHubRequestCreator requestCreator)
    {
        this.requestCreator = requestCreator;
    }


    @Required
    public void setY2YSyncLoggingCtxFactory(Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory)
    {
        this.y2YSyncLoggingCtxFactory = y2YSyncLoggingCtxFactory;
    }
}
