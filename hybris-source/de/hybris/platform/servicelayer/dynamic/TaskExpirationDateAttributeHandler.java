package de.hybris.platform.servicelayer.dynamic;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.task.TaskModel;
import java.util.Date;

public class TaskExpirationDateAttributeHandler implements DynamicAttributeHandler<Date, TaskModel>
{
    public Date get(TaskModel model)
    {
        Preconditions.checkNotNull(model);
        Long expirationTimeMillis = model.getExpirationTimeMillis();
        return (expirationTimeMillis != null) ? new Date(expirationTimeMillis.longValue()) : null;
    }


    public void set(TaskModel model, Date date)
    {
        Preconditions.checkNotNull(model);
        model.setExpirationTimeMillis((date == null) ? null : Long.valueOf(date.getTime()));
    }
}
