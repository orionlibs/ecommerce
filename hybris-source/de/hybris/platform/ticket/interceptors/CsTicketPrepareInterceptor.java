package de.hybris.platform.ticket.interceptors;

import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.springframework.beans.factory.annotation.Required;

public class CsTicketPrepareInterceptor implements PrepareInterceptor, InitDefaultsInterceptor
{
    private CsTicketState initialTicketState;
    private KeyGenerator keyGenerator;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CsTicketModel)
        {
            CsTicketModel ticket = (CsTicketModel)model;
            ticket.setState(this.initialTicketState);
        }
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CsTicketModel)
        {
            CsTicketModel ticket = (CsTicketModel)model;
            if(ticket.getTicketID() == null)
            {
                ticket.setTicketID(createTicketId());
            }
            if(ticket.getState() == null)
            {
                ticket.setState(this.initialTicketState);
            }
        }
    }


    protected String createTicketId()
    {
        return this.keyGenerator.generate().toString();
    }


    @Required
    public void setInitialTicketState(CsTicketState initialTicketState)
    {
        this.initialTicketState = initialTicketState;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
