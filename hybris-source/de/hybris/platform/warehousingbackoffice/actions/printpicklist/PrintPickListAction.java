package de.hybris.platform.warehousingbackoffice.actions.printpicklist;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintPickListAction implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PrintPickListAction.class);
    protected static final String PICKING_TEMPLATE_CODE = "NPR_Picking";
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintPickSlipStrategy;
    @Resource
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        ActionResult<ConsignmentModel> result = new ActionResult("success");
        ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
        WorkflowActionModel pickWorkflowAction = getWarehousingConsignmentWorkflowService().getWorkflowActionForTemplateCode("NPR_Picking", consignment);
        if(pickWorkflowAction != null && !WorkflowActionStatus.COMPLETED.equals(pickWorkflowAction.getStatus()))
        {
            getWarehousingConsignmentWorkflowService().decideWorkflowAction(consignment, "NPR_Picking", null);
        }
        LOG.info("Generating Pick Label for consignment {}", consignment.getCode());
        getConsignmentPrintPickSlipStrategy().printDocument(consignment);
        return result;
    }


    public boolean canPerform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        Object data = consignmentModelActionContext.getData();
        return (data instanceof ConsignmentModel && ((ConsignmentModel)consignmentModelActionContext.getData()).getFulfillmentSystemConfig() == null);
    }


    public boolean needsConfirmation(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return null;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintPickSlipStrategy()
    {
        return this.consignmentPrintPickSlipStrategy;
    }


    protected WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
    {
        return this.warehousingConsignmentWorkflowService;
    }
}
