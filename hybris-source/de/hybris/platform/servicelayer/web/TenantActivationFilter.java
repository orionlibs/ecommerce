package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

@Deprecated(since = "ages", forRemoval = true)
public class TenantActivationFilter extends GenericFilterBean
{
    private final String tenantId;
    private static final Logger LOG = Logger.getLogger(TenantActivationFilter.class.getName());


    public TenantActivationFilter()
    {
        this("master");
    }


    public TenantActivationFilter(String tenantId)
    {
        Tenant configuredTenant = Registry.getTenantByID(tenantId);
        if(configuredTenant == null)
        {
            throw new IllegalArgumentException("Configured tenant " + tenantId + " does not exist!");
        }
        this.tenantId = tenantId;
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        setCurrentTenant();
        try
        {
            filterChain.doFilter(request, response);
        }
        finally
        {
            unsetCurrentTenant();
        }
    }


    protected void setCurrentTenant()
    {
        try
        {
            Registry.setCurrentTenantByID(this.tenantId);
        }
        catch(IllegalStateException exp)
        {
            LOG.warn("activateTenant() called twice!", exp);
        }
    }


    public void unsetCurrentTenant()
    {
        Registry.unsetCurrentTenant();
    }


    public String toString()
    {
        return "TenantProcessor[ tenant: " + this.tenantId + ", hashCode: " + hashCode() + " ]";
    }
}
