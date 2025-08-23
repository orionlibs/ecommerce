package de.hybris.platform.webservicescommons.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;

public class ExceptionTranslationFilter extends GenericFilterBean
{
    private HandlerExceptionResolver restHandlerExceptionResolver;


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException
    {
        try
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch(NestedServletException ex)
        {
            if(ex.getRootCause() instanceof Exception)
            {
                this.restHandlerExceptionResolver.resolveException((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, null, (Exception)ex
                                .getRootCause());
            }
            else
            {
                throw ex;
            }
        }
        catch(RuntimeException | ServletException | java.io.IOException ex)
        {
            this.restHandlerExceptionResolver.resolveException((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, null, ex);
        }
    }


    protected HandlerExceptionResolver getRestHandlerExceptionResolver()
    {
        return this.restHandlerExceptionResolver;
    }


    @Required
    public void setRestHandlerExceptionResolver(HandlerExceptionResolver restHandlerExceptionResolver)
    {
        this.restHandlerExceptionResolver = restHandlerExceptionResolver;
    }
}
