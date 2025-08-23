package de.hybris.platform.core;

import de.hybris.bootstrap.ddl.PropertiesLoader;
import java.util.Map;
import java.util.Objects;

public class TenantPropertiesLoader implements PropertiesLoader
{
    private final Tenant tenant;


    public TenantPropertiesLoader(Tenant tenant)
    {
        Objects.requireNonNull(tenant);
        this.tenant = tenant;
    }


    public String getProperty(String key)
    {
        return this.tenant.getConfig().getParameter(key);
    }


    public String getProperty(String key, String defaultValue)
    {
        return this.tenant.getConfig().getString(key, defaultValue);
    }


    public Map<String, String> getAllProperties()
    {
        return this.tenant.getConfig().getAllParameters();
    }
}
