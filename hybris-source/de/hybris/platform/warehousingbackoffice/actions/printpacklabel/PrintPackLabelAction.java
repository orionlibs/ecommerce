package de.hybris.platform.warehousingbackoffice.actions.printpacklabel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintPackLabelAction implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PrintPackLabelAction.class);
    protected static final String PACK_CONSIGNMENT_CHOICE = "packConsignment";
    protected static final String PACKING_TEMPLATE_CODE = "NPR_Packing";
    protected static final String CAPTURE_PAYMENT_ON_CONSIGNMENT = "warehousing.capturepaymentonconsignment";
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintPackSlipStrategy;
    @Resource
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;
    @Resource
    private ConfigurationService configurationService;


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
        LOG.info("Generating Pack Label for consignment {}", consignment.getCode());
        WorkflowActionModel packWorkflowAction = getWarehousingConsignmentWorkflowService().getWorkflowActionForTemplateCode("NPR_Packing", consignment);
        if(packWorkflowAction != null && !WorkflowActionStatus.COMPLETED.equals(packWorkflowAction.getStatus()))
        {
            try
            {
                getWarehousingConsignmentWorkflowService()
                                .decideWorkflowAction(consignment, "NPR_Packing", "packConsignment");
            }
            catch(BusinessProcessException e)
            {
                LOG.info("Unable to trigger pack consignment process for consignment: {}", consignment.getCode());
            }
        }
        getConsignmentPrintPackSlipStrategy().printDocument(consignment);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        Object data = consignmentModelActionContext.getData();
        return (data instanceof ConsignmentModel && getConfigurationService().getConfiguration()
                        .getBoolean("warehousing.capturepaymentonconsignment", false) &&
                        !ConsignmentStatus.CANCELLED.equals(((ConsignmentModel)data).getStatus()) && ((ConsignmentModel)data).getFulfillmentSystemConfig() == null);
    }


    public boolean needsConfirmation(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return null;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintPackSlipStrategy()
    {
        return this.consignmentPrintPackSlipStrategy;
    }


    protected WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
    {
        return this.warehousingConsignmentWorkflowService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }
}
