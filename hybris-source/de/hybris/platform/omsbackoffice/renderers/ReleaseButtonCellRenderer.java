package de.hybris.platform.omsbackoffice.renderers;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class ReleaseButtonCellRenderer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseButtonCellRenderer.class);
    private BusinessProcessService businessProcessService;


    protected boolean canPerformOperation(ConsignmentModel consignmentModel, ConsignmentStatus consignmentStatus)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentModel", consignmentModel);
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentStatus", consignmentStatus);
        return consignmentStatus.equals(consignmentModel.getStatus());
    }


    protected void triggerBusinessProcessEvent(ConsignmentModel consignmentModel, String eventName, String eventChoice)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentModel", consignmentModel);
        ServicesUtil.validateParameterNotNullStandardMessage("eventName", eventName);
        ServicesUtil.validateParameterNotNullStandardMessage("eventChoice", eventChoice);
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug(String.format("Manually releasing consignment with code: %s", new Object[] {consignmentModel.getCode()}));
        }
        Optional<ConsignmentProcessModel> model = consignmentModel.getConsignmentProcesses().stream().findFirst();
        String processCode = model.isPresent() ? ((ConsignmentProcessModel)model.get()).getCode() : "";
        getBusinessProcessService().triggerEvent(BusinessProcessEvent.builder(processCode + "_" + processCode).withChoice(eventChoice)
                        .withEventTriggeringInTheFutureDisabled().build());
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }
}
