package de.hybris.platform.warehousingbackoffice.actions.exportforms;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportFormsAction implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(ExportFormsAction.class);
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintExportFormStrategy;


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
        LOG.info("Generating Export Form for consignment {}", consignment.getCode());
        getConsignmentPrintExportFormStrategy().printDocument(consignment);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return (consignmentModelActionContext.getData() != null && ((ConsignmentModel)consignmentModelActionContext
                        .getData()).getFulfillmentSystemConfig() == null);
    }


    public boolean needsConfirmation(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        return null;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintExportFormStrategy()
    {
        return this.consignmentPrintExportFormStrategy;
    }
}
