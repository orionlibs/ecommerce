package de.hybris.platform.task.action;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.action.plain.TriggeredPlainAction;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.springframework.beans.factory.annotation.Required;

public class ActionPerformableTaskRunner implements TaskRunner<TaskModel>
{
    private ActionExecutionStrategy executionStrategy;


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        AbstractActionModel action = (AbstractActionModel)task.getContextItem();
        Object argument = task.getContext();
        TriggeredPlainAction prep = (TriggeredPlainAction)getExecutionStrategy().prepareAction(action, argument);
        try
        {
            getExecutionStrategy().triggerAction((TriggeredAction)prep);
        }
        catch(ActionException e)
        {
            if(e.getCause() instanceof RetryLaterException)
            {
                throw (RetryLaterException)e.getCause();
            }
            throw e;
        }
    }


    @Required
    public void setExecutionStrategy(ActionExecutionStrategy executionStrategy)
    {
        this.executionStrategy = executionStrategy;
    }


    protected ActionExecutionStrategy getExecutionStrategy()
    {
        return this.executionStrategy;
    }
}
