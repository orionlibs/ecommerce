package de.hybris.platform.webservicescommons.filter;

import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;

public class RestSessionFilter extends GenericFilterBean
{
    private SessionService sessionService;


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        Session session = null;
        try
        {
            session = this.sessionService.createNewSession();
            filterChain.doFilter(request, response);
        }
        finally
        {
            if(session != null)
            {
                this.sessionService.closeSession(session);
            }
            HttpSession httpSession = httpRequest.getSession(false);
            if(httpSession != null)
            {
                httpSession.invalidate();
            }
        }
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
