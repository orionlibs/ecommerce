package de.hybris.platform.warehousingbackoffice.actions.printreturnshippinglabel;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.warehousingbackoffice.labels.strategy.ConsignmentPrintDocumentStrategy;
import javax.annotation.Resource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintReturnShippingLabelAction implements CockpitAction<ConsignmentModel, ConsignmentModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PrintReturnShippingLabelAction.class);
    protected static final String CAN_PERFORM_PROP_KEY = "warehousing.printreturnshippinglabel.active";
    @Resource
    private ConsignmentPrintDocumentStrategy consignmentPrintReturnShippingLabelStrategy;
    @Resource
    private ConfigurationService configurationService;


    public ActionResult<ConsignmentModel> perform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        ConsignmentModel consignment = (ConsignmentModel)consignmentModelActionContext.getData();
        LOG.info("Generate return shipping label for consignment {}", consignment.getCode());
        getConsignmentPrintReturnShippingLabelStrategy().printDocument(consignment);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<ConsignmentModel> consignmentModelActionContext)
    {
        boolean result = false;
        Configuration configuration = getConfigurationService().getConfiguration();
        try
        {
            if(configuration != null)
            {
                result = configuration.getBoolean("warehousing.printreturnshippinglabel.active");
            }
        }
        catch(ConversionException | java.util.NoSuchElementException e)
        {
            LOG.error(String.format("No or incorrect property defined for [%s]. Value has to be 'true' or 'false' - any other value will be treated as a false", new Object[] {"warehousing.printreturnshippinglabel.active"}));
        }
        return (result && consignmentModelActionContext.getData() != null && ((ConsignmentModel)consignmentModelActionContext
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected ConsignmentPrintDocumentStrategy getConsignmentPrintReturnShippingLabelStrategy()
    {
        return this.consignmentPrintReturnShippingLabelStrategy;
    }
}
