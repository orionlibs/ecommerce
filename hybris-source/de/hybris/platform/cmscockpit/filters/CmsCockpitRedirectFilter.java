package de.hybris.platform.cmscockpit.filters;

import de.hybris.platform.util.Config;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

public class CmsCockpitRedirectFilter extends GenericFilterBean
{
    private static final String HTTPS = "https:";
    private static final String HTTP = "http:";
    private static final String TOMCAT_SSL_PORT = "tomcat.ssl.port";


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        if(!request.isSecure())
        {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            String requestUrl = httpRequest.getRequestURL().toString();
            requestUrl = requestUrl.replace("http:", "https:").replace("" + httpRequest.getLocalPort(), getPort());
            httpResponse.sendRedirect(requestUrl);
        }
        else
        {
            filterChain.doFilter(request, response);
        }
    }


    protected String getPort()
    {
        return Config.getParameter("tomcat.ssl.port");
    }
}
