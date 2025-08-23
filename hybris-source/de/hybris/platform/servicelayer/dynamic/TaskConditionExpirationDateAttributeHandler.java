package de.hybris.platform.servicelayer.dynamic;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.task.TaskConditionModel;
import java.util.Date;

public class TaskConditionExpirationDateAttributeHandler implements DynamicAttributeHandler<Date, TaskConditionModel>
{
    public Date get(TaskConditionModel model)
    {
        Preconditions.checkNotNull(model);
        Long milliSeconds = model.getExpirationTimeMillis();
        return (milliSeconds != null) ? new Date(milliSeconds.longValue()) : null;
    }


    public void set(TaskConditionModel model, Date date)
    {
        Preconditions.checkNotNull(model);
        model.setExpirationTimeMillis((date == null) ? null : Long.valueOf(date.getTime()));
    }
}
