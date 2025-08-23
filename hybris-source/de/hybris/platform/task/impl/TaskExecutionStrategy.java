package de.hybris.platform.task.impl;

import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.Date;

public interface TaskExecutionStrategy
{
    void run(TaskService paramTaskService, TaskRunner<TaskModel> paramTaskRunner, TaskModel paramTaskModel) throws RetryLaterException;


    Throwable handleError(TaskService paramTaskService, TaskRunner<TaskModel> paramTaskRunner, TaskModel paramTaskModel, Throwable paramThrowable);


    void finished(TaskService paramTaskService, TaskRunner<TaskModel> paramTaskRunner, TaskModel paramTaskModel, Throwable paramThrowable);


    Date handleRetry(TaskService paramTaskService, TaskRunner<TaskModel> paramTaskRunner, TaskModel paramTaskModel, RetryLaterException paramRetryLaterException, int paramInt);


    Class<? extends TaskRunner<? extends TaskModel>> getRunnerClass();
}
