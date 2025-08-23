package de.hybris.platform.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GZIPServletFilter implements Filter
{
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        String ae = request.getHeader("accept-encoding");
        if(ae != null && ae.indexOf("gzip") != -1)
        {
            GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
            chain.doFilter(req, (ServletResponse)wrappedResponse);
            wrappedResponse.finishResponse();
            return;
        }
        chain.doFilter(req, res);
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
    }


    public void destroy()
    {
    }
}
