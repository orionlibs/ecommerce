package de.hybris.platform.servicelayer.web.session;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class NoSessionRepositoryFilter implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        filterChain.doFilter(servletRequest, servletResponse);
    }


    public void destroy()
    {
    }
}
