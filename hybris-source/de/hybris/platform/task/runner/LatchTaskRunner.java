package de.hybris.platform.task.runner;

import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;

public class LatchTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = Logger.getLogger(LatchTaskRunner.class);
    private CountDownLatch latch;


    public void handleError(TaskService service, TaskModel task, Throwable fault)
    {
        LOG.error("Failed to run task '" + task + "' (context: " + task.getContext() + ").", fault);
    }


    public void run(TaskService service, TaskModel task) throws RetryLaterException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Running on '" + task + "' (context: " + task.getContext() + "').");
        }
        if(this.latch != null)
        {
            this.latch.countDown();
        }
    }


    public void setLatch(CountDownLatch latch)
    {
        this.latch = latch;
    }
}
