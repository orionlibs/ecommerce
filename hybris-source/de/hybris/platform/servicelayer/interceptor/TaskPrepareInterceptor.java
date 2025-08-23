package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.task.TaskModel;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TaskPrepareInterceptor implements PrepareInterceptor<TaskModel>
{
    private static final long NEAR_INFINITE_EXECUTION_TIME = 7258161600000L;


    public void onPrepare(TaskModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(ctx.isNew(model))
        {
            model.setRunningOnClusterNode(Integer.valueOf(-1));
        }
        setRunningOnCluseterModeIfNull(model);
        setFailedFalseIfNull(model);
        setCurrentExecutionTimeIfNull(model);
        setZeroRetryIfNull(model);
        setInfiniteExpirationTimeIfNull(model);
        model.setExecutionHourMillis(
                        Long.valueOf(Instant.ofEpochMilli(model.getExecutionTimeMillis().longValue()).truncatedTo(ChronoUnit.HOURS).toEpochMilli()));
    }


    private static void setRunningOnCluseterModeIfNull(TaskModel model)
    {
        if(model.getRunningOnClusterNode() == null)
        {
            model.setRunningOnClusterNode(Integer.valueOf(-1));
        }
    }


    private static void setZeroRetryIfNull(TaskModel model)
    {
        if(model.getRetry() == null)
        {
            model.setRetry(Integer.valueOf(0));
        }
    }


    private static void setCurrentExecutionTimeIfNull(TaskModel model)
    {
        if(model.getExecutionTimeMillis() == null)
        {
            model.setExecutionTimeMillis(Long.valueOf(System.currentTimeMillis()));
        }
    }


    private static void setInfiniteExpirationTimeIfNull(TaskModel model)
    {
        if(model.getExpirationTimeMillis() == null)
        {
            model.setExpirationTimeMillis(Long.valueOf(7258161600000L));
        }
    }


    private static void setFailedFalseIfNull(TaskModel model)
    {
        if(model.getFailed() == null)
        {
            model.setFailed(Boolean.FALSE);
        }
    }
}
