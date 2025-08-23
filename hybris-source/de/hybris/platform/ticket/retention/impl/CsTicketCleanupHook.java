package de.hybris.platform.ticket.retention.impl;

import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.springframework.beans.factory.annotation.Required;

public class CsTicketCleanupHook implements ItemCleanupHook<CsTicketModel>
{
    private DefaultCsTicketCleanupStrategy csTicketCleanupStrategy;


    public void cleanupRelatedObjects(CsTicketModel csTicketModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("csTicketModel", csTicketModel);
        getCsTicketCleanupStrategy().cleanupRelatedObjects(csTicketModel);
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
}
