package de.hybris.platform.processing.distributed.defaultimpl;

import de.hybris.platform.processing.model.DistributedProcessTransitionTaskModel;
import de.hybris.platform.processing.model.DistributedProcessWorkerTaskModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import java.util.Objects;

public class Dispatcher implements TaskRunner<TaskModel>
{
    private final TransitionsHandler transitionsHandler;
    private final ExecutionHandler executionHandler;
    private final DistributedProcessLoggingCtxFactory distributedProcessLoggingCtxFactory;


    public Dispatcher(TransitionsHandler transitionsHandler, ExecutionHandler executionHandler, DistributedProcessLoggingCtxFactory distributedProcessLoggingCtxFactory)
    {
        Objects.requireNonNull(transitionsHandler, "transitionsHandler mustn't be null");
        Objects.requireNonNull(executionHandler, "executionHandler mustn't be null");
        this.transitionsHandler = transitionsHandler;
        this.executionHandler = executionHandler;
        this.distributedProcessLoggingCtxFactory = distributedProcessLoggingCtxFactory;
    }


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        if(task instanceof DistributedProcessTransitionTaskModel)
        {
            this.transitionsHandler.runTransitionTask((DistributedProcessTransitionTaskModel)task);
        }
        else if(task instanceof DistributedProcessWorkerTaskModel)
        {
            this.executionHandler.runWorkerTask((DistributedProcessWorkerTaskModel)task);
        }
        else
        {
            throw new UnsupportedOperationException("This runner is not designed to handle " + task);
        }
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        if(task instanceof DistributedProcessTransitionTaskModel)
        {
            this.transitionsHandler.handleErrorDuringTransition((DistributedProcessTransitionTaskModel)task);
        }
        else if(task instanceof DistributedProcessWorkerTaskModel)
        {
            this.executionHandler.handleErrorDuringBatchExecution((DistributedProcessWorkerTaskModel)task);
        }
        else
        {
            throw new UnsupportedOperationException("This runner is not designed to handle " + task);
        }
    }


    public boolean isLoggingSupported()
    {
        return false;
    }


    public TaskLoggingCtx initLoggingCtx(TaskModel task)
    {
        return this.distributedProcessLoggingCtxFactory.createLoggingCtxForTask(task);
    }


    public void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
        if(taskCtx != null)
        {
            taskCtx.finishAndClose();
        }
    }
}
