package de.hybris.platform.servicelayer.dynamic;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.task.TaskModel;
import java.util.Date;

public class TaskExecutionDateAttributeHandler implements DynamicAttributeHandler<Date, TaskModel>
{
    public Date get(TaskModel model)
    {
        Preconditions.checkNotNull(model);
        Long executionTimeMillis = model.getExecutionTimeMillis();
        return (executionTimeMillis != null) ? new Date(executionTimeMillis.longValue()) : null;
    }


    public void set(TaskModel model, Date date)
    {
        Preconditions.checkNotNull(model);
        model.setExecutionTimeMillis((date == null) ? null : Long.valueOf(date.getTime()));
    }
}
