package de.hybris.platform.task;

import java.util.Date;

public interface TaskService
{
    public static final String BEAN_ID = "taskService";


    void triggerEvent(String paramString);


    void triggerEvent(String paramString, Date paramDate);


    default boolean triggerEvent(TaskEvent event)
    {
        throw new IllegalStateException("method should be implemented in subclass");
    }


    void scheduleTask(TaskModel paramTaskModel);


    TaskEngine getEngine();
}
