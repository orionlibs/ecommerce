package de.hybris.platform.ticket.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CsCustomerEventPrepareInterceptor implements PrepareInterceptor
{
    private TicketService ticketService;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CsCustomerEventModel)
        {
            CsCustomerEventModel event = (CsCustomerEventModel)model;
            CsTicketModel ticket = getTicketService().getTicketForTicketEvent((CsTicketEventModel)event);
            if(canSubjectBePopulated(event, ticket))
            {
                event.setSubject(ticket.getHeadline());
            }
        }
    }


    protected boolean canSubjectBePopulated(CsCustomerEventModel event, CsTicketModel ticket)
    {
        return (StringUtils.isNotBlank(event.getText()) && ticket != null);
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
}
