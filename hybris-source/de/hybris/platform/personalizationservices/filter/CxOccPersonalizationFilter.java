package de.hybris.platform.personalizationservices.filter;

import de.hybris.platform.personalizationservices.handlers.PersonalizationHandler;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class CxOccPersonalizationFilter extends OncePerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(CxOccPersonalizationFilter.class);
    private PersonalizationHandler personalizationHandler;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            this.personalizationHandler.handlePersonalization(request, response);
        }
        catch(RuntimeException e)
        {
            LOG.warn("Handling personalization in CxOccPersonalizationFilter failed", e);
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }


    protected PersonalizationHandler getPersonalizationHandler()
    {
        return this.personalizationHandler;
    }


    public void setPersonalizationHandler(PersonalizationHandler personalizationHandler)
    {
        this.personalizationHandler = personalizationHandler;
    }
}
