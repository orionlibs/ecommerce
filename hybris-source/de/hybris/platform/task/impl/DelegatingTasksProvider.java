package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import java.util.List;

public abstract class DelegatingTasksProvider implements TasksProvider
{
    private final TasksProvider delegate;


    protected DelegatingTasksProvider(TasksProvider delegate)
    {
        this.delegate = delegate;
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        return getTasksProvider().getTasksToSchedule(runtimeConfigHolder, taskEngineParameters, maxItemsToSchedule);
    }


    public void afterTaskFinished(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
        getTasksProvider().afterTaskFinished(taskPk, runtimeConfigHolder);
    }


    public void afterTaskUnlocked(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
        getTasksProvider().afterTaskUnlocked(taskPk, runtimeConfigHolder);
    }


    public void beforeTaskEngineStart(int nodeId)
    {
        getTasksProvider().beforeTaskEngineStart(nodeId);
    }


    public void afterTaskEngineStop(int nodeId, RuntimeConfigHolder runtimeConfigHolder)
    {
        getTasksProvider().afterTaskEngineStop(nodeId, runtimeConfigHolder);
    }


    public int getMaxItemsToSchedule(DefaultTaskService.TaskEngineRunningState runningState, RuntimeConfigHolder runtimeConfigHolder)
    {
        return getTasksProvider().getMaxItemsToSchedule(runningState, runtimeConfigHolder);
    }


    protected TasksProvider getTasksProvider()
    {
        return this.delegate;
    }
}
