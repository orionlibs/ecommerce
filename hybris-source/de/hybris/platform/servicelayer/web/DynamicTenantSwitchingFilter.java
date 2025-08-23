package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WebSessionFunctions;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

@Deprecated(since = "5.0", forRemoval = true)
public class DynamicTenantSwitchingFilter extends OncePerRequestFilter
{
    private static final Logger LOG = Logger.getLogger(DynamicTenantSwitchingFilter.class.getName());


    @Deprecated(since = "ages", forRemoval = true)
    public DynamicTenantSwitchingFilter(String tenantIDPattern)
    {
    }


    public DynamicTenantSwitchingFilter()
    {
    }


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        String urlPatternTenantId = Utilities.getTenantIdForContext(request.getContextPath(), "master");
        String storedTenant = fetchHttpSessionTenantID(request);
        killSessionIfNecessary(urlPatternTenantId, storedTenant, request);
        String tenantId = determineTenantIDForRequest(urlPatternTenantId, storedTenant);
        try
        {
            setCurrentTenant(tenantId);
            storeHttpSessionTenantID(request, tenantId);
            filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
        }
        finally
        {
            unsetCurrentTenant();
        }
    }


    private String fetchHttpSessionTenantID(HttpServletRequest request)
    {
        return (String)request.getSession().getAttribute("tenantID");
    }


    private void storeHttpSessionTenantID(HttpServletRequest httpRequest, String tenantId)
    {
        httpRequest.getSession().setAttribute("tenantID", tenantId);
    }


    private void killSessionIfNecessary(String urlPatternTenantId, String storedTenant, HttpServletRequest httpRequest)
    {
        if(StringUtils.isNotEmpty(urlPatternTenantId) && storedTenant != null &&
                        !urlPatternTenantId.equalsIgnoreCase(storedTenant))
        {
            try
            {
                setCurrentTenant(storedTenant);
                WebSessionFunctions.invalidateSession(httpRequest.getSession());
            }
            finally
            {
                unsetCurrentTenant();
            }
        }
    }


    private void setCurrentTenant(String tenantId)
    {
        Registry.setCurrentTenantByID(tenantId);
    }


    private void unsetCurrentTenant()
    {
        Registry.unsetCurrentTenant();
    }


    private String determineTenantIDForRequest(String urlTenantID, String storedTenantID)
    {
        String tenantID = "master";
        if(urlTenantID != null)
        {
            tenantID = urlTenantID;
        }
        else if(storedTenantID != null)
        {
            tenantID = storedTenantID;
        }
        return tenantID;
    }
}
