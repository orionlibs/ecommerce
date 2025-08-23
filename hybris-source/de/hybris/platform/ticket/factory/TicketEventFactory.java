package de.hybris.platform.ticket.factory;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;

public interface TicketEventFactory
{
    CsTicketEventModel createEvent(String paramString);


    CsTicketEventModel ensureTicketSetup(CsTicketEventModel paramCsTicketEventModel, String paramString);
}
