package de.hybris.platform.ticket.retention.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CsTicketsByCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
    private DefaultCsTicketCleanupStrategy csTicketCleanupStrategy;
    private ModelService modelService;


    public void cleanupRelatedObjects(CustomerModel customerModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("customerModel", customerModel);
        if(CollectionUtils.isNotEmpty(customerModel.getTickets()))
        {
            customerModel.getTickets().forEach(csTicketModel -> getCsTicketCleanupStrategy().cleanupRelatedObjects(csTicketModel));
            getModelService().removeAll(customerModel.getTickets());
        }
    }


    protected DefaultCsTicketCleanupStrategy getCsTicketCleanupStrategy()
    {
        return this.csTicketCleanupStrategy;
    }


    @Required
    public void setCsTicketCleanupStrategy(DefaultCsTicketCleanupStrategy csTicketCleanupStrategy)
    {
        this.csTicketCleanupStrategy = csTicketCleanupStrategy;
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
}
