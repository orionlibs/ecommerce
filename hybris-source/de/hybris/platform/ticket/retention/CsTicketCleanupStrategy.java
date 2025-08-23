package de.hybris.platform.ticket.retention;

import de.hybris.platform.ticket.model.CsTicketModel;

public interface CsTicketCleanupStrategy
{
    void cleanupRelatedObjects(CsTicketModel paramCsTicketModel);
}
