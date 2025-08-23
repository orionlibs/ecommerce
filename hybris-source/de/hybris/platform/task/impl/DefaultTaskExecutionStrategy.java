package de.hybris.platform.task.impl;

import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTaskExecutionStrategy implements TaskExecutionStrategy
{
    private ModelService modelService;
    private Class<? extends TaskRunner<? extends TaskModel>> runnerClass;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void finished(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model, Throwable error)
    {
        try
        {
            getModelService().remove(model);
        }
        catch(ModelRemovalException e)
        {
            Logger.getLogger(getClass()).warn("could not remove finished task " + model + " due to " + e.getMessage());
        }
    }


    public Throwable handleError(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model, Throwable error)
    {
        try
        {
            runner.handleError(taskService, model, error);
        }
        catch(Exception exception)
        {
            return exception;
        }
        if(error instanceof de.hybris.platform.task.TaskInterruptedException)
        {
            return (Throwable)new RetryLaterException(error.getMessage());
        }
        return error;
    }


    public void run(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model) throws RetryLaterException
    {
        RetryLaterException exception;
        TaskLoggingCtx loggingCtx = null;
        try
        {
            loggingCtx = runner.initLoggingCtx(model);
            exception = (RetryLaterException)Transaction.current().execute((TransactionBody)new Object(this, runner, taskService, model));
        }
        catch(RetryLaterException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
        finally
        {
            runner.stopLoggingCtx(loggingCtx);
        }
        if(exception != null)
        {
            throw exception;
        }
    }


    public Date handleRetry(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model, RetryLaterException retry, int currentRetries)
    {
        Date next = retry.getMethod().nextInvocation(new Date(), currentRetries, retry.getDelay());
        Logger log = Logger.getLogger(getClass());
        if(log.isDebugEnabled())
        {
            log.debug("Retrying task #" + model.getPk() + " later: " + next);
        }
        return next;
    }


    public void setRunnerClass(Class<? extends TaskRunner<? extends TaskModel>> runnerClass)
    {
        this.runnerClass = runnerClass;
    }


    public Class<? extends TaskRunner<? extends TaskModel>> getRunnerClass()
    {
        return this.runnerClass;
    }
}
