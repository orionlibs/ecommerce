package de.hybris.y2ysync.task.runner;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.logging.Y2YSyncLoggingCtxFactory;
import de.hybris.y2ysync.task.runner.internal.ImportPackage;
import de.hybris.y2ysync.task.runner.internal.ImportZipCreator;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ImportZipCreatorTaskRunner extends AbstractMainTaskRunner implements TaskRunner<TaskModel>
{
    private Y2YSyncDAO y2YSyncDAO;
    private ImportZipCreator importZipCreator;
    private Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory;
    private final Logger LOG = LoggerFactory.getLogger(ImportZipCreator.class);


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        String syncExecutionId = getSyncExecutionId(task);
        ImportPackage importPackage = createImportPackage(syncExecutionId);
        this.LOG.info("Created ZIP import package for: {}", syncExecutionId);
        updateSyncCronJobStatus(syncExecutionId, importPackage);
    }


    private ImportPackage createImportPackage(String syncExecutionId)
    {
        return this.importZipCreator.createImportMedias(syncExecutionId);
    }


    private void updateSyncCronJobStatus(String syncExecutionId, ImportPackage importPackage)
    {
        Y2YSyncCronJobModel cronJobModel = this.y2YSyncDAO.findSyncCronJobByCode(syncExecutionId);
        cronJobModel.setImpexZip(importPackage.getMediaData());
        cronJobModel.setMediasZip(importPackage.getMediaBinaries());
        cronJobModel.setStatus(CronJobStatus.FINISHED);
        this.modelService.save(cronJobModel);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        RetryLaterException ex = new RetryLaterException("Re-scheduling task");
        ex.setRollBack(false);
        ex.setDelay(Duration.ofSeconds(30L).toMillis());
        throw ex;
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


    @Required
    public void setY2YSyncDAO(Y2YSyncDAO y2YSyncDAO)
    {
        this.y2YSyncDAO = y2YSyncDAO;
    }


    @Required
    public void setImportZipCreator(ImportZipCreator importZipCreator)
    {
        this.importZipCreator = importZipCreator;
    }


    @Required
    public void setY2YSyncLoggingCtxFactory(Y2YSyncLoggingCtxFactory y2YSyncLoggingCtxFactory)
    {
        this.y2YSyncLoggingCtxFactory = y2YSyncLoggingCtxFactory;
    }
}
