package de.hybris.platform.ticket.retention.impl;

import de.hybris.platform.directpersistence.audit.dao.WriteAuditRecordsDAO;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.retention.CsTicketCleanupStrategy;
import de.hybris.platform.ticket.service.TicketService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCsTicketCleanupStrategy implements CsTicketCleanupStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultCsTicketCleanupStrategy.class);
    private TicketService ticketService;
    private ModelService modelService;
    private WriteAuditRecordsDAO writeAuditRecordsDAO;


    public void cleanupRelatedObjects(CsTicketModel csTicketModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("csTicketModel", csTicketModel);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning up ticket related objects for: " + csTicketModel);
        }
        List<CsTicketEventModel> eventModels = getTicketService().getEventsForTicket(csTicketModel);
        eventModels.stream().forEach(event -> {
            event.getEntries().stream().forEach(());
            getModelService().remove(event);
            getWriteAuditRecordsDAO().removeAuditRecordsForType("CsTicketEvent", event.getPk());
        });
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


    protected TicketService getTicketService()
    {
        return this.ticketService;
    }


    @Required
    public void setTicketService(TicketService ticketService)
    {
        this.ticketService = ticketService;
    }


    protected WriteAuditRecordsDAO getWriteAuditRecordsDAO()
    {
        return this.writeAuditRecordsDAO;
    }


    @Required
    public void setWriteAuditRecordsDAO(WriteAuditRecordsDAO writeAuditRecordsDAO)
    {
        this.writeAuditRecordsDAO = writeAuditRecordsDAO;
    }
}
