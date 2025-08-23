package de.hybris.y2ysync.task.logging;

import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.logging.MediaFileLoggingCtx;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.y2ysync.task.runner.TaskContext;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Required;

public class Y2YSyncLoggingCtxFactory
{
    public static final String THREAD_ID = "threadId";
    public static final String PROCESS_EXECUTION_ID = "processExecutionId";
    public static final String CLUSTER_ID = "clusterId";
    private String logLevel;
    private String loggerName;
    private ModelService modelService;
    private MediaService mediaService;
    private ClusterService clusterService;
    private CronJobService cronJobService;


    public TaskLoggingCtx createY2YSyncTaskLogger(TaskModel task)
    {
        TaskContext taskContext = extractContextFromModel(task);
        setLogContext(taskContext);
        String filename = taskContext.getSyncExecutionId() + "_" + taskContext.getSyncExecutionId() + "_log_" + task.getRunnerBean();
        Level effectiveLogLevel = Level.toLevel(this.logLevel);
        return (TaskLoggingCtx)new MediaFileLoggingCtx(taskContext.getSyncExecutionId(), filename, effectiveLogLevel, this.loggerName, this.modelService, this.cronJobService, this.mediaService);
    }


    public void finish(TaskLoggingCtx taskCtx)
    {
        clearLogContext();
        if(taskCtx != null)
        {
            taskCtx.finishAndClose();
        }
    }


    private void setLogContext(TaskContext taskContext)
    {
        MDC.put("threadId", Thread.currentThread().getName());
        MDC.put("processExecutionId", taskContext.getSyncExecutionId());
        MDC.put("clusterId", getClusterId());
    }


    private void clearLogContext()
    {
        MDC.remove("threadId");
        MDC.remove("processExecutionId");
        MDC.remove("clusterId");
    }


    private String getClusterId()
    {
        if(this.clusterService.isClusteringEnabled())
        {
            return String.valueOf(this.clusterService.getClusterId());
        }
        return String.valueOf(-1);
    }


    private TaskContext extractContextFromModel(TaskModel task)
    {
        Map<String, Object> context;
        Object ctxObject = task.getContext();
        if(ctxObject == null)
        {
            throw new SystemException("Task doesn't have a context");
        }
        try
        {
            context = (Map<String, Object>)ctxObject;
        }
        catch(ClassCastException e)
        {
            throw new SystemException("Couldn't cast a context to an appropriate type", e);
        }
        return new TaskContext(this.modelService, this.mediaService, context);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
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
