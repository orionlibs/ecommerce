package de.hybris.y2ysync.task.distributed;

import de.hybris.platform.processing.distributed.defaultimpl.LoggingCtxProducer;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.logging.MediaFileLoggingCtx;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.y2ysync.model.Y2YDistributedProcessModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Required;

public class DistributedY2YSyncLoggingCtxProducer implements LoggingCtxProducer
{
    private String logLevel;
    private String loggerName;
    private MediaService mediaService;
    private ModelService modelService;
    private CronJobService cronJobService;


    public boolean isDefinedForTask(TaskModel task)
    {
        if(task instanceof DistributedProcessWorkerTaskModel)
        {
            DistributedProcessWorkerTaskModel workerTask = (DistributedProcessWorkerTaskModel)task;
            if(workerTask.getContextItem() instanceof de.hybris.y2ysync.model.Y2YBatchModel)
            {
                return true;
            }
        }
        return false;
    }


    public TaskLoggingCtx createLoggingCtx(TaskModel task)
    {
        DistributedProcessWorkerTaskModel workerTask = (DistributedProcessWorkerTaskModel)task;
        Y2YSyncCronJobModel y2ySyncCronJob = ((Y2YDistributedProcessModel)workerTask.getContextItem().getProcess()).getY2ySyncCronJob();
        BatchModel contextItem = workerTask.getContextItem();
        String logCode = getUniqueCode(contextItem) + "_log";
        Level effectiveLogLevel = Level.toLevel(this.logLevel);
        return (TaskLoggingCtx)new MediaFileLoggingCtx(y2ySyncCronJob.getCode(), logCode, effectiveLogLevel, this.loggerName, this.modelService, this.cronJobService, this.mediaService);
    }


    private String getUniqueCode(BatchModel inputBatch)
    {
        return inputBatch.getProcess().getCode() + "_" + inputBatch.getProcess().getCode() + "_" + inputBatch.getExecutionId();
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
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
    public void setLogLevel(String logLevel)
    {
        this.logLevel = logLevel;
    }


    @Required
    public void setLoggerName(String loggerName)
    {
        this.loggerName = loggerName;
    }
}
