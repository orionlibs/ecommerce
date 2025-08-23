package de.hybris.platform.personalizationservices.filter;

import de.hybris.platform.personalizationservices.handlers.PersonalizationHandler;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

public class CxPersonalizationFilter extends OncePerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(CxPersonalizationFilter.class);
    private CxRecalculationService cxRecalculationService;
    private PersonalizationHandler personalizationHandler;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            this.personalizationHandler.handlePersonalization(request, response);
        }
        catch(RuntimeException e)
        {
            LOG.warn("Personalization handling error", e);
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }


    @Deprecated(since = "2005", forRemoval = true)
    public void setCxRecalculationService(CxRecalculationService cxRecalculationService)
    {
        this.cxRecalculationService = cxRecalculationService;
    }


    @Deprecated(since = "2005", forRemoval = true)
    protected CxRecalculationService getCxRecalculationService()
    {
        return this.cxRecalculationService;
    }


    protected PersonalizationHandler getPersonalizationHandler()
    {
        return this.personalizationHandler;
    }


    @Required
    public void setPersonalizationHandler(PersonalizationHandler personalizationHandler)
    {
        this.personalizationHandler = personalizationHandler;
    }
}
