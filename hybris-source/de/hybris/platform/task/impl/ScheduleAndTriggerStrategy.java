package de.hybris.platform.task.impl;

import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskModel;
import java.util.Date;

public interface ScheduleAndTriggerStrategy
{
    boolean triggerEvent(String paramString);


    boolean triggerEvent(String paramString, Date paramDate);


    default boolean triggerEvent(TaskEvent event)
    {
        throw new IllegalStateException("method should be implemented in subclass");
    }


    void scheduleTask(TaskModel paramTaskModel);


    void setTaskDao(TaskDAO paramTaskDAO);
}
