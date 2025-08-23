package de.hybris.platform.task.action;

import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.model.TriggerTaskModel;
import de.hybris.platform.util.Config;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class TriggerTaskRunner implements TaskRunner<TriggerTaskModel>
{
    private static final String UNEXPECTED_EXCEPTION_MSG = "Unexpected exception.";
    private static final String RESCHEDULING_TASK = "Re-scheduling task";
    private static final String RESCHEDULING_TASK_DUE_ERR = "Re-scheduling task due to error";
    private static final Logger LOG = Logger.getLogger(TriggerTaskRunner.class.getName());
    private ModelService modelService;
    private TriggerService triggerService;


    public void run(TaskService taskService, TriggerTaskModel task)
    {
        TriggerModel tr = task.getTrigger();
        Date previousActivationTime = tr.getActivationTime();
        Date now = new Date();
        if(previousActivationTime != null && previousActivationTime.after(now))
        {
            LOG.info("Postponing execution of task " + task.getPk() + " because trigger's activationTime (" + previousActivationTime + ") is in the future (now is " + now + ")");
            throw createRetryLaterException("Re-scheduling task", tr.getActivationTime(), null);
        }
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Handling task (retry nr: " + task.getRetry() + ") for " + getCronjobOrJobInfo(tr));
            }
            this.triggerService.activate(tr);
            this.modelService.refresh(tr);
            task.setProperty("retry", Integer.valueOf(0));
            this.modelService.save(task);
        }
        catch(Exception e)
        {
            int maxRetries = Config.getInt("taskRunner.trigger.retry.maxattempts", 10);
            int nrOfRetry = task.getRetry().intValue() + 1;
            if(nrOfRetry < maxRetries)
            {
                if(checkIfNextExecutionTimeIsNotRecalcualted(previousActivationTime, tr.getActivationTime()))
                {
                    LOG.warn("Unexpected exception. Using default retry interval. Trying to retry nr: " + nrOfRetry, e);
                    throw createRetryLaterExceptionWithLinearTimeAndException("Re-scheduling task due to error", e);
                }
                LOG.warn("Unexpected exception. Trying to retry nr: " + nrOfRetry, e);
                throw createRetryLaterException("Re-scheduling task due to error", tr.getActivationTime(), e);
            }
            LOG.error("Unexpected exception. Max retries reached: " + nrOfRetry + " Stopping next execution", e);
            throw createRetryLaterException("Re-scheduling task due to error", new Date(Long.MAX_VALUE), e);
        }
        throw createRetryLaterException("Re-scheduling task", tr.getActivationTime(), null);
    }


    private boolean checkIfNextExecutionTimeIsNotRecalcualted(Date previousActivationTime, Date nextActivationTime)
    {
        return (previousActivationTime != null && previousActivationTime.equals(nextActivationTime));
    }


    private RetryLaterException createRetryLaterExceptionWithLinearTimeAndException(String info, Exception e)
    {
        long maxSecondsToRetry = Config.getLong("taskRunner.trigger.retry.interval.seconds", 300L);
        RetryLaterException ex = new RetryLaterException(info, e);
        ex.setRollBack(false);
        ex.setMethod(RetryLaterException.Method.LINEAR);
        ex.setDelay(maxSecondsToRetry * 1000L);
        return ex;
    }


    private RetryLaterException createRetryLaterException(String info, Date nextTime, Exception e)
    {
        RetryLaterException ex = new RetryLaterException(info, e);
        ex.setRollBack(false);
        ex.setMethod(RetryLaterException.Method.EXACT_DATE);
        ex.setDelay((nextTime == null) ? (new Date(Long.MAX_VALUE)).getTime() : nextTime.getTime());
        return ex;
    }


    public void handleError(TaskService taskService, TriggerTaskModel task, Throwable error)
    {
        LOG.info("###error handling for task: " + task + "###");
        LOG.info("###" + error + "###");
    }


    private String getCronjobOrJobInfo(TriggerModel trigger)
    {
        if(trigger.getCronJob() != null)
        {
            return "cronjob '" + trigger.getCronJob().getCode() + "'";
        }
        if(trigger.getJob() != null)
        {
            return "job '" + trigger.getJob().getCode() + "'";
        }
        return "n/a - neither job nor cronjob exists for trigger!";
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTriggerService(TriggerService triggerService)
    {
        this.triggerService = triggerService;
    }
}
