package de.hybris.platform.warehousing.taskassignment.actions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

public class TaskAssignmentPickupConsignmentAction extends AbstractTaskAssignmentActions
{
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String CONFIRM_PICKUP_CONSIGNMENT_CHOICE = "confirmPickupConsignment";
    protected static final String PICKUP_TEMPLATE_CODE = "NPR_Pickup";


    public WorkflowDecisionModel perform(WorkflowActionModel workflowAction)
    {
        WorkflowDecisionModel result = null;
        if(getAttachedConsignment(workflowAction).isPresent())
        {
            ConsignmentModel attachedConsignment = getAttachedConsignment(workflowAction).get();
            getWorkflowActionAndAssignPrincipal("NPR_Pickup", workflowAction, attachedConsignment, "picked up");
            getConsignmentBusinessProcessService()
                            .triggerChoiceEvent((ItemModel)attachedConsignment, "ConsignmentActionEvent", "confirmPickupConsignment");
            result = workflowAction.getDecisions().isEmpty() ? null : workflowAction.getDecisions().iterator().next();
        }
        return result;
    }
}
