package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import java.util.List;

public interface TasksProvider
{
    public static final int MAX_ITEMS_DEFAULT_MULTIPLIER = 2;


    List<VersionPK> getTasksToSchedule(RuntimeConfigHolder paramRuntimeConfigHolder, TaskEngineParameters paramTaskEngineParameters, int paramInt);


    default void afterTaskFinished(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
    }


    default void afterTaskUnlocked(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
    }


    default void beforeTaskEngineStart(int nodeId)
    {
    }


    default void afterTaskEngineStop(int nodeId, RuntimeConfigHolder runtimeConfigHolder)
    {
    }


    default int getMaxItemsToSchedule(DefaultTaskService.TaskEngineRunningState runningState, RuntimeConfigHolder runtimeConfigHolder)
    {
        return runningState.getMaxThreads() * 2;
    }
}
