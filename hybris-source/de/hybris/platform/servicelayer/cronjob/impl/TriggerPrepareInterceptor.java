package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.model.TriggerTaskModel;
import org.springframework.beans.factory.annotation.Required;

public class TriggerPrepareInterceptor implements PrepareInterceptor<TriggerModel>
{
    private ModelService modelService;


    public void onPrepare(TriggerModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(ctx.isNew(model))
        {
            TriggerTaskModel task = (TriggerTaskModel)this.modelService.create(TriggerTaskModel.class);
            task.setTrigger(model);
            pinTaskToNodeIfNecessary(model, task);
            pinTaskToNodeGroupIfNecessary(model, task);
            if(model.getActivationTime() != null)
            {
                task.setExecutionTimeMillis(Long.valueOf(model.getActivationTime().getTime()));
            }
            ctx.registerElementFor(task, PersistenceOperation.SAVE);
        }
    }


    private void pinTaskToNodeIfNecessary(TriggerModel trigger, TriggerTaskModel task)
    {
        if(trigger.getCronJob() != null && trigger.getCronJob().getNodeID() != null)
        {
            task.setNodeId(trigger.getCronJob().getNodeID());
        }
        else if(trigger.getJob() != null && trigger.getJob().getNodeID() != null)
        {
            task.setNodeId(trigger.getJob().getNodeID());
        }
    }


    private void pinTaskToNodeGroupIfNecessary(TriggerModel trigger, TriggerTaskModel task)
    {
        if(trigger.getCronJob() != null && trigger.getCronJob().getNodeGroup() != null)
        {
            task.setNodeGroup(trigger.getCronJob().getNodeGroup());
        }
        else if(trigger.getJob() != null && trigger.getJob().getNodeGroup() != null)
        {
            task.setNodeGroup(trigger.getJob().getNodeGroup());
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
