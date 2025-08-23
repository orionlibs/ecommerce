package de.hybris.platform.webservicescommons.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SessionHidingFilter implements Filter
{
    public void destroy()
    {
    }


    public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)paramServletRequest;
        paramFilterChain.doFilter((ServletRequest)new SessionHidingRequestWrapper(req), paramServletResponse);
    }


    public void init(FilterConfig paramFilterConfig)
    {
    }
}
