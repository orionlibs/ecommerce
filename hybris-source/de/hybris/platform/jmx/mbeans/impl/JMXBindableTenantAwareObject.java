package de.hybris.platform.jmx.mbeans.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;

public class JMXBindableTenantAwareObject
{
    private static final String NO_TENANT = "INACTIVE";
    private String tenantId;
    private final String jmxDomain = "hybris";
    private String jmxPath;


    protected String getTenantId()
    {
        return this.tenantId;
    }


    public String getJmxPath()
    {
        return this.jmxPath;
    }


    public void setJmxPath(String jmxPath)
    {
        this.jmxPath = jmxPath;
    }


    protected void setTenant(Tenant tenant)
    {
        Preconditions.checkNotNull("Initial tenant id can not be null ");
        this.tenantId = tenant.getTenantID();
    }


    public String getObjectNameString()
    {
        if(getTenantId() != null)
        {
            String str = getJmxDomain() + ":tenantscope=" + getJmxDomain();
            return str + "," + str;
        }
        String objectname = getJmxDomain() + ":tenantscope=" + getJmxDomain();
        return objectname + "," + objectname;
    }


    public String getJmxDomain()
    {
        return "hybris";
    }
}
