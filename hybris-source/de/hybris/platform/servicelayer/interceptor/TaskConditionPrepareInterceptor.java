package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.task.TaskConditionModel;

public class TaskConditionPrepareInterceptor implements PrepareInterceptor<TaskConditionModel>
{
    private static final long NEAR_INFINITE_EXECUTION_TIME = 7258161600000L;


    public void onPrepare(TaskConditionModel model, InterceptorContext ctx) throws InterceptorException
    {
        setFullfilledFalseIfNull(model);
        setConsumedFalseIfNull(model);
        setInfiniteExpirationTimeIfNull(model);
    }


    private static void setConsumedFalseIfNull(TaskConditionModel model)
    {
        if(model.getConsumed() == null)
        {
            model.setConsumed(Boolean.FALSE);
        }
    }


    private static void setFullfilledFalseIfNull(TaskConditionModel model)
    {
        if(model.getFulfilled() == null)
        {
            model.setFulfilled(Boolean.FALSE);
        }
    }


    private static void setInfiniteExpirationTimeIfNull(TaskConditionModel model)
    {
        if(model.getExpirationTimeMillis() == null)
        {
            model.setExpirationTimeMillis(Long.valueOf(7258161600000L));
        }
    }
}
