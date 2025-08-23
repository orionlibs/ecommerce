package de.hybris.platform.warehousing.taskassignment.actions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskAssignmentPackConsignmentAction extends AbstractTaskAssignmentActions
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssignmentPackConsignmentAction.class);
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String PACK_CONSIGNMENT_CHOICE = "packConsignment";
    protected static final String SHIPPING_DECISION = "Shipping";
    protected static final String PACKING_TEMPLATE_CODE = "NPR_Packing";


    public WorkflowDecisionModel perform(WorkflowActionModel workflowAction)
    {
        Optional<ItemModel> attachedConsignmentOptional = getAttachedConsignment(workflowAction);
        WorkflowDecisionModel result = null;
        if(attachedConsignmentOptional.isPresent())
        {
            Optional<WorkflowDecisionModel> decisionModel;
            ConsignmentModel attachedConsignment = (ConsignmentModel)attachedConsignmentOptional.get();
            getWorkflowActionAndAssignPrincipal(workflowAction, attachedConsignment, "packed");
            getConsignmentBusinessProcessService()
                            .triggerChoiceEvent((ItemModel)attachedConsignment, "ConsignmentActionEvent", "packConsignment");
            boolean shippingConsignment = (attachedConsignment.getDeliveryPointOfService() == null);
            if(shippingConsignment)
            {
                decisionModel = workflowAction.getDecisions().stream().filter(decision -> "Shipping".equals(((WorkflowActionModel)decision.getToActions().iterator().next()).getName())).findFirst();
            }
            else
            {
                decisionModel = workflowAction.getDecisions().stream().filter(decision -> !"Shipping".equals(((WorkflowActionModel)decision.getToActions().iterator().next()).getName())).findFirst();
            }
            result = decisionModel.isPresent() ? decisionModel.get() : null;
        }
        return result;
    }


    protected void getWorkflowActionAndAssignPrincipal(WorkflowActionModel workflowAction, ConsignmentModel attachedConsignment, String actionLabel)
    {
        WorkflowActionModel packingWorkflowAction = getWarehousingConsignmentWorkflowService().getWorkflowActionForTemplateCode("NPR_Packing", attachedConsignment);
        assignNewPrincipalToAction(workflowAction, packingWorkflowAction);
        LOGGER.info("Consignment: {} has been {} by: {}", new Object[] {attachedConsignment.getCode(), actionLabel, packingWorkflowAction
                        .getPrincipalAssigned().getDisplayName()});
    }
}
