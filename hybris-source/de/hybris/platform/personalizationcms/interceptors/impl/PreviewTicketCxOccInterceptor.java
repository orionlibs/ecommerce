package de.hybris.platform.personalizationcms.interceptors.impl;

import de.hybris.platform.personalizationservices.occ.impl.AbstractCxOccInterceptor;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.servicelayer.session.SessionService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Required;

public class PreviewTicketCxOccInterceptor extends AbstractCxOccInterceptor
{
    public static final int PRECEDENCE = 1;
    private SessionService sessionService;


    public PreviewTicketCxOccInterceptor()
    {
        super(1);
    }


    public CxOccInterceptorVote shouldPersonalizeRequestVote(HttpServletRequest request)
    {
        CxOccInterceptorVote result = getDefaultVote(request);
        if(isEnabled())
        {
            result.setConclusive(true);
            result.setVote(true);
        }
        return result;
    }


    protected boolean isEnabled()
    {
        return (getSessionService().getAttribute("cmsTicketId") != null);
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
