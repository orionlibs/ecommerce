package de.hybris.platform.task.action;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.ActionExecutionException;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class TaskActionExecutionStrategy extends AbstractBusinessService implements ActionExecutionStrategy
{
    private static final Set<ActionType> ACCEPTED = Collections.singleton(ActionType.TASK);
    private TaskService taskService;
    private ActionTaskCreationStrategy creationStrategy;


    public Set<ActionType> getAcceptedTypes()
    {
        return ACCEPTED;
    }


    public void isActionValid(ActionType type, String target) throws InvalidActionException
    {
    }


    public <T> TriggeredAction<T> prepareAction(AbstractActionModel action, T argument) throws ActionException
    {
        TaskModel task = getCreationStrategy().createTaskForAction(action, argument);
        return (TriggeredAction<T>)new TriggeredTaskAction(action, argument, this, task);
    }


    public <T> void triggerAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        TaskModel task = ((TriggeredTaskAction)preparedAction).getTask();
        try
        {
            if(!getModelService().isUpToDate(task))
            {
                getModelService().save(task);
            }
            getTaskService().scheduleTask(task);
        }
        catch(Throwable t)
        {
            throw new ActionExecutionException("error scheduling " + preparedAction.getAction() + " using task " + task + " : " + t
                            .getMessage(), t, preparedAction);
        }
    }


    public <T> void cancelAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        TaskModel task = ((TriggeredTaskAction)preparedAction).getTask();
        if(!getModelService().isNew(task))
        {
            try
            {
                getModelService().remove(task);
            }
            catch(Exception e)
            {
                throw new ActionException("error removing prepared task " + task + " due to " + e.getMessage(), e);
            }
        }
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }


    protected TaskService getTaskService()
    {
        return this.taskService;
    }


    @Required
    public void setCreationStrategy(ActionTaskCreationStrategy creationStrategy)
    {
        this.creationStrategy = creationStrategy;
    }


    protected ActionTaskCreationStrategy getCreationStrategy()
    {
        return this.creationStrategy;
    }
}
