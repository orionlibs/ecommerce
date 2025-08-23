package de.hybris.deltadetection.interceptors;

import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import org.springframework.beans.factory.annotation.Required;

public class StreamConfigurationRemoveInterceptor implements RemoveInterceptor<StreamConfigurationModel>
{
    private ModelService modelService;
    private TaskService taskService;


    public void onRemove(StreamConfigurationModel model, InterceptorContext ctx) throws InterceptorException
    {
        this.taskService.scheduleTask(prepareTaskModel(model.getStreamId()));
    }


    private TaskModel prepareTaskModel(String streamId)
    {
        TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
        task.setRunnerBean("removeOrphanedVersionMarkersTaskRunner");
        task.setContext(streamId);
        return task;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }
}
