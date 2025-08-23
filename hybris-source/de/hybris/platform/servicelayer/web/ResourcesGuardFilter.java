package de.hybris.platform.servicelayer.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;

public class ResourcesGuardFilter extends GenericFilterBean
{
    private ResourcesGuardService resourcesGuardService;
    private String extensionName;
    private String reditectTo;
    private static final Logger LOG = LoggerFactory.getLogger(ResourcesGuardFilter.class);


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        String resourcePath = getResourcePath(httpRequest);
        if(this.resourcesGuardService.isResourceEnabled(this.extensionName, resourcePath))
        {
            filterChain.doFilter((ServletRequest)httpRequest, (ServletResponse)httpResponse);
        }
        else
        {
            LOG.warn("An attempt to access {}, which has been disabled via endpoint.(.*?).disabled property", resourcePath);
            if(StringUtils.isNotEmpty(this.reditectTo))
            {
                httpResponse.sendError(404, this.reditectTo);
            }
            else
            {
                httpResponse.setStatus(404);
            }
        }
    }


    private String getResourcePath(HttpServletRequest request)
    {
        String contextPath = request.getServletContext().getContextPath();
        return StringUtils.substringAfter(request.getRequestURI(), contextPath);
    }


    @Required
    public void setResourcesGuardService(ResourcesGuardService resourcesGuardService)
    {
        this.resourcesGuardService = resourcesGuardService;
    }


    @Required
    public void setExtensionName(String extensionName)
    {
        this.extensionName = extensionName;
    }


    public void setReditectTo(String reditectTo)
    {
        this.reditectTo = reditectTo;
    }
}
