package de.hybris.platform.task;

import de.hybris.platform.task.logging.TaskLoggingCtx;

public interface TaskRunner<T extends TaskModel>
{
    default boolean isLoggingSupported()
    {
        return false;
    }


    default TaskLoggingCtx initLoggingCtx(T task)
    {
        return () -> {
        };
    }


    default void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
    }


    void run(TaskService paramTaskService, T paramT) throws RetryLaterException;


    void handleError(TaskService paramTaskService, T paramT, Throwable paramThrowable);
}
