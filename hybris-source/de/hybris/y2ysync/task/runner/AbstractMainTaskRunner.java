package de.hybris.y2ysync.task.runner;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractMainTaskRunner
{
    protected ModelService modelService;


    protected String getSyncExecutionId(TaskModel task)
    {
        Object context = task.getContext();
        Preconditions.checkState(context instanceof Map, "Wrong context object in a task: " + task + ", should be Map but found: " + context);
        Object value = ((Map)context).get("syncExecutionID");
        Preconditions.checkState(value instanceof String);
        Preconditions.checkNotNull(value, "synJobID not found in the context (current context: " + context + ")");
        return (String)value;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
