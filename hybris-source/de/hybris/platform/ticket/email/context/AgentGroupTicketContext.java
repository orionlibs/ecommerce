package de.hybris.platform.ticket.email.context;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

public class AgentGroupTicketContext extends AbstractTicketContext
{
    public AgentGroupTicketContext(CsTicketModel ticket, CsTicketEventModel event)
    {
        super(ticket, event);
    }


    public String getName()
    {
        return getTicket().getAssignedGroup().getDisplayName();
    }


    public String getTo()
    {
        return (getTicket().getAssignedGroup() != null && getTicket().getAssignedGroup().getEmailDistributionList() != null) ? getTicket()
                        .getAssignedGroup().getEmailDistributionList() : null;
    }
}
