package de.hybris.platform.impex.distributed.log;

import com.google.common.base.Preconditions;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.processing.distributed.defaultimpl.LoggingCtxProducer;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.logging.MediaFileLoggingCtx;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Required;

public class DistributedImpexLoggingCtxProducer implements LoggingCtxProducer
{
    private MediaService mediaService;
    private ModelService modelService;
    private CronJobService cronJobService;
    private String logLevel;
    private String loggerName;


    public boolean isDefinedForTask(TaskModel task)
    {
        if(task instanceof DistributedProcessWorkerTaskModel)
        {
            DistributedProcessWorkerTaskModel workerTask = (DistributedProcessWorkerTaskModel)task;
            if(workerTask.getContextItem() instanceof ImportBatchModel)
            {
                return true;
            }
        }
        return false;
    }


    public TaskLoggingCtx createLoggingCtx(TaskModel task)
    {
        DistributedProcessWorkerTaskModel workerTask = (DistributedProcessWorkerTaskModel)task;
        ImpExImportCronJobModel impExImportCronJob = asDistributedImportProcessModel(workerTask.getContextItem().getProcess()).getImpExImportCronJob();
        ImportBatchModel inputBatch = (ImportBatchModel)task.getContextItem();
        String logCode = getUniqueCode((BatchModel)inputBatch) + "_log";
        Level effectiveLogLevel = Level.toLevel(this.logLevel);
        return (TaskLoggingCtx)new MediaFileLoggingCtx(impExImportCronJob.getCode(), logCode, effectiveLogLevel, this.loggerName, this.modelService, this.cronJobService, this.mediaService);
    }


    private DistributedImportProcessModel asDistributedImportProcessModel(DistributedProcessModel process)
    {
        Preconditions.checkState(process instanceof DistributedImportProcessModel, "process must be instance of DistributedImportProcessModel");
        return (DistributedImportProcessModel)process;
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
