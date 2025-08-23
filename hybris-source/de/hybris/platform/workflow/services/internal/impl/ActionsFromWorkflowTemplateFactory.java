package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ActionsFromWorkflowTemplateFactory extends AbstractWorkflowFactory implements WorkflowFactory<WorkflowModel, WorkflowTemplateModel, List<WorkflowActionModel>>
{
    private WorkflowActionService workflowActionService;


    @Required
    public void setWorkflowActionService(WorkflowActionService workflowActionService)
    {
        this.workflowActionService = workflowActionService;
    }


    public List<WorkflowActionModel> create(WorkflowModel target, WorkflowTemplateModel template)
    {
        List<WorkflowActionModel> workflowActions = new ArrayList<>();
        for(WorkflowActionTemplateModel action : template.getActions())
        {
            WorkflowActionModel newWorkflowAction = this.workflowActionService.createWorkflowAction(action, target);
            this.modelService.save(newWorkflowAction);
            workflowActions.add(newWorkflowAction);
        }
        return workflowActions;
    }
}
