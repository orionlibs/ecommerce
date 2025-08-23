package de.hybris.platform.task.action;

import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.TaskModel;

public class TriggeredTaskAction<T> extends TriggeredAction<T>
{
    private final TaskModel task;


    public TriggeredTaskAction(AbstractActionModel action, T argument, ActionExecutionStrategy strategy, TaskModel task)
    {
        super(action, argument, strategy);
        ServicesUtil.validateParameterNotNull(task, "Parameter 'task' was null");
        this.task = task;
    }


    public TaskModel getTask()
    {
        return this.task;
    }
}
