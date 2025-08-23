package de.hybris.platform.processengine.task.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.impl.DefaultTaskExecutionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessengineTaskExecutionStrategy extends DefaultTaskExecutionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessengineTaskExecutionStrategy.class);


    public void run(TaskService taskService, TaskRunner<TaskModel> runner, TaskModel model) throws RetryLaterException
    {
        try
        {
            LOG.debug("Running task: {} using runner: {} and task service: {}", new Object[] {model, runner.getClass(), taskService.getClass()});
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
