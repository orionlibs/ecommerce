package de.hybris.platform.ticket.strategies;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;

public interface TicketRenderStrategy
{
    String renderTicketEvent(CsTicketEventModel paramCsTicketEventModel);
}
