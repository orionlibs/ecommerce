package de.hybris.platform.warehousing.taskassignment.actions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractTaskAssignmentActions implements AutomatedWorkflowTemplateJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTaskAssignmentActions.class);
    private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
    private ModelService modelService;
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;


    protected Optional<ItemModel> getAttachedConsignment(WorkflowActionModel action)
    {
        return action.getAttachmentItems().stream().filter(item -> item instanceof ConsignmentModel).findFirst();
    }


    protected Optional<ItemModel> getAttachedAsn(WorkflowActionModel action)
    {
        return action.getAttachmentItems().stream().filter(item -> item instanceof de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel).findFirst();
    }


    protected void assignNewPrincipalToAction(WorkflowActionModel automatedWorkflowAction, WorkflowActionModel workflowAction)
    {
        if(!automatedWorkflowAction.getPrincipalAssigned().equals(workflowAction.getPrincipalAssigned()))
        {
            automatedWorkflowAction.setPrincipalAssigned(workflowAction.getPrincipalAssigned());
            getModelService().save(automatedWorkflowAction);
        }
    }


    protected void getWorkflowActionAndAssignPrincipal(String templateCode, WorkflowActionModel currentWorkflowAction, ConsignmentModel attachedConsignment, String actionLabel)
    {
        WorkflowActionModel workflowAction = getWarehousingConsignmentWorkflowService().getWorkflowActionForTemplateCode(templateCode, attachedConsignment);
        assignNewPrincipalToAction(currentWorkflowAction, workflowAction);
        LOGGER.info("Consignment: {} has been {} by: {}", new Object[] {attachedConsignment.getCode(), actionLabel, workflowAction
                        .getPrincipalAssigned().getDisplayName()});
    }


    protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    @Required
    public void setConsignmentBusinessProcessService(WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
    {
        this.consignmentBusinessProcessService = consignmentBusinessProcessService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
    {
        return this.warehousingConsignmentWorkflowService;
    }


    @Required
    public void setWarehousingConsignmentWorkflowService(WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService)
    {
        this.warehousingConsignmentWorkflowService = warehousingConsignmentWorkflowService;
    }
}
