package de.hybris.platform.servicelayer.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaNotAuthorizedFilter implements Filter
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaNotAuthorizedFilter.class);


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse)
        {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            SecureResponseWrapper secureResponseWrapper = new SecureResponseWrapper((HttpServletResponse)response);
            String mediaPKStr = httpRequest.getParameter("mediaPK");
            LOG.debug("User not authorized for given media (PK={})", mediaPKStr);
            sendNotAuthorizedResponseStatus((HttpServletResponse)secureResponseWrapper);
        }
        else
        {
            chain.doFilter(request, response);
        }
    }


    private void sendNotAuthorizedResponseStatus(HttpServletResponse httpResponse)
    {
        httpResponse.setStatus(401);
    }


    public void destroy()
    {
    }


    public void init(FilterConfig config) throws ServletException
    {
    }
}
