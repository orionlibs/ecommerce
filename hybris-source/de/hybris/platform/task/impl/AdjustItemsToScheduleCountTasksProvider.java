package de.hybris.platform.task.impl;

import java.util.List;

public class AdjustItemsToScheduleCountTasksProvider extends DelegatingTasksProvider
{
    private final RuntimeConfigHolder.IntTaskEngineProperty relativeMaxItemsToScheduleMultiplier;


    public AdjustItemsToScheduleCountTasksProvider(TasksProvider delegate, RuntimeConfigHolder.IntTaskEngineProperty relativeMaxItemsToScheduleMultiplier)
    {
        super(delegate);
        this.relativeMaxItemsToScheduleMultiplier = relativeMaxItemsToScheduleMultiplier;
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        return super.getTasksToSchedule(runtimeConfigHolder, taskEngineParameters, adjustItemsToSchedule(runtimeConfigHolder, maxItemsToSchedule));
    }


    private int adjustItemsToSchedule(RuntimeConfigHolder runtimeConfigHolder, int maxItemsToSchedule)
    {
        int multiplier = Math.max(1, ((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)this.relativeMaxItemsToScheduleMultiplier)).intValue());
        return maxItemsToSchedule * multiplier;
    }
}
