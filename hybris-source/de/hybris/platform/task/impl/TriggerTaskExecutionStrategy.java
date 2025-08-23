package de.hybris.platform.task.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

public class TriggerTaskExecutionStrategy extends DefaultTaskExecutionStrategy
{
    public void run(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model) throws RetryLaterException
    {
        try
        {
            runner.run(taskService, model);
        }
        catch(RetryLaterException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }
}
