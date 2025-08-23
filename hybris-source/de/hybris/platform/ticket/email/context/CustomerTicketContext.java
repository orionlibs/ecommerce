package de.hybris.platform.ticket.email.context;

import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

public class CustomerTicketContext extends AbstractTicketContext
{
    public CustomerTicketContext(CsTicketModel ticket, CsTicketEventModel event)
    {
        super(ticket, event);
    }


    public String getName()
    {
        return getTicket().getCustomer().getName();
    }


    public String getTo()
    {
        return (getTicket().getCustomer() != null && getTicket().getCustomer().getDefaultPaymentAddress() != null) ? getTicket()
                        .getCustomer().getDefaultPaymentAddress().getEmail() : null;
    }
}
