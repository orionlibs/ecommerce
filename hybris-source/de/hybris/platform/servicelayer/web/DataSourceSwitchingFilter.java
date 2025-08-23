package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

public class DataSourceSwitchingFilter extends GenericFilterBean
{
    private static final Logger LOG = Logger.getLogger(DataSourceSwitchingFilter.class.getName());


    public DataSourceSwitchingFilter()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating new DataSourceSwitchingFilter");
        }
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        handleSlaveDataSourceSettings(httpRequest, httpResponse);
        filterChain.doFilter(request, response);
    }


    private void handleSlaveDataSourceSettings(HttpServletRequest request, HttpServletResponse response)
    {
        Tenant tenant = getCurrentTenant();
        String slaveMode = request.getParameter("slave");
        if(!StringUtils.isBlank(slaveMode))
        {
            if("true".equalsIgnoreCase(slaveMode))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Slave mode requested: trying to activate the slave dataSource...");
                }
                String assignedID = getSlaveDataSourceID(request);
                if(assignedID != null)
                {
                    try
                    {
                        tenant.activateSlaveDataSource(assignedID);
                    }
                    catch(IllegalArgumentException e)
                    {
                        assignedID = null;
                    }
                }
                if(assignedID == null)
                {
                    assignedID = tenant.activateSlaveDataSource();
                    setSlaveDataSourceID(assignedID, response);
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Slave dataSource activated. ID=" + assignedID);
                }
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Slave mode deactivating requested: no slave dataSource will be used");
                }
                setSlaveDataSourceID(null, response);
            }
        }
        else
        {
            String assignedID = getSlaveDataSourceID(request);
            if(assignedID != null)
            {
                try
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Cookie info detected  for slave mode - trying to activated the slave dataSource: " + assignedID);
                    }
                    tenant.activateSlaveDataSource(assignedID);
                }
                catch(IllegalArgumentException e)
                {
                    setSlaveDataSourceID(null, response);
                }
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("No Cookie info detected for slave mode - slave dataSource will not be activated");
            }
        }
    }


    private void setSlaveDataSourceID(String id, HttpServletResponse response)
    {
        Cookie cookie = new Cookie("use.slave.datasource", id);
        cookie.setMaxAge((id == null) ? 0 : -1);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    private String getSlaveDataSourceID(HttpServletRequest request)
    {
        String id = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
        {
            for(Cookie cookie : cookies)
            {
                if("use.slave.datasource".equalsIgnoreCase(cookie.getName()))
                {
                    id = cookie.getValue();
                }
            }
        }
        return id;
    }


    private Tenant getCurrentTenant()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant == null)
        {
            throw new IllegalStateException("no tenant active");
        }
        return tenant;
    }
}
