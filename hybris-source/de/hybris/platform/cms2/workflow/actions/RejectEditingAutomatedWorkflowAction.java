package de.hybris.platform.cms2.workflow.actions;

import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class RejectEditingAutomatedWorkflowAction implements AutomatedWorkflowTemplateJob
{
    private WorkflowActionService workflowActionService;


    public WorkflowDecisionModel perform(WorkflowActionModel action)
    {
        Predicate<WorkflowActionModel> isActionActive = actionModel -> getWorkflowActionService().isActive(actionModel);
        action.getWorkflow().getActions().stream()
                        .filter(isActionActive)
                        .filter(activeAction -> !activeAction.equals(action))
                        .forEach(activeAction -> getWorkflowActionService().idle(activeAction));
        return action.getDecisions().stream().findFirst().orElse(null);
    }


    protected WorkflowActionService getWorkflowActionService()
    {
        return this.workflowActionService;
    }


    @Required
    public void setWorkflowActionService(WorkflowActionService workflowActionService)
    {
        this.workflowActionService = workflowActionService;
    }
}
