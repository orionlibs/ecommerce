package de.hybris.platform.ticket.email.context;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

public class AgentTicketContext extends AbstractTicketContext
{
    public AgentTicketContext(CsTicketModel ticket, CsTicketEventModel event)
    {
        super(ticket, event);
    }


    public String getName()
    {
        return getTicket().getAssignedAgent().getName();
    }


    public String getTo()
    {
        return (getTicket().getAssignedAgent() != null && getTicket().getAssignedAgent().getDefaultPaymentAddress() != null) ? getTicket()
                        .getAssignedAgent().getDefaultPaymentAddress().getEmail() :
                        null;
    }
}
