package de.hybris.platform.task;

public interface ExtendedTaskRunner<T extends TaskModel> extends TaskRunner<T>
{
    boolean keepFinishedTask(TaskService paramTaskService, TaskModel paramTaskModel);


    boolean keepErrorTask(TaskService paramTaskService, TaskModel paramTaskModel, Throwable paramThrowable);


    boolean captureLogs(TaskService paramTaskService, TaskModel paramTaskModel);
}
