package de.hybris.platform.processengine.process;

import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassthroughTaskRunner implements TaskRunner<ProcessTaskModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PassthroughTaskRunner.class);


    public void run(TaskService taskService, ProcessTaskModel task) throws RetryLaterException
    {
        LOG.info("ProcessTask {} has been processed by PassthroughTaskRunner", task.getPk());
    }


    public void handleError(TaskService taskService, ProcessTaskModel task, Throwable error)
    {
    }
}
