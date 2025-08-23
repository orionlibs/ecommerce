package de.hybris.platform.ticket.strategies;

import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

public interface TicketEventEmailStrategy
{
    void sendEmailsForEvent(CsTicketModel paramCsTicketModel, CsTicketEventModel paramCsTicketEventModel);


    void sendEmailsForAssignAgentTicketEvent(CsTicketModel paramCsTicketModel, CsTicketEventModel paramCsTicketEventModel, CsEmailRecipients paramCsEmailRecipients);
}
