package de.hybris.platform.task.impl;

import java.util.Collections;
import java.util.List;

class ShufflingTasksProvider extends DelegatingTasksProvider
{
    ShufflingTasksProvider(TasksProvider internalTasksProvider)
    {
        super(internalTasksProvider);
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        List<TasksProvider.VersionPK> tasksToSchedule = getTasksProvider().getTasksToSchedule(runtimeConfigHolder, taskEngineParameters, maxItemsToSchedule);
        return (tasksToSchedule != null) ? shuffleItems(tasksToSchedule) : Collections.<TasksProvider.VersionPK>emptyList();
    }


    private static List<TasksProvider.VersionPK> shuffleItems(List<TasksProvider.VersionPK> items)
    {
        Collections.shuffle(items);
        return items;
    }
}
