package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractWorkflowFactory
{
    protected ModelService modelService;


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected WorkflowActionModel getWorkAction(WorkflowActionTemplateModel templateAction, List<WorkflowActionModel> workflowActions)
    {
        for(WorkflowActionModel act : workflowActions)
        {
            if(act.getTemplate().equals(templateAction))
            {
                return act;
            }
        }
        return null;
    }
}
