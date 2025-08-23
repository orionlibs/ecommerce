package de.hybris.bootstrap.config.tenants;

import java.util.List;
import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public Tenants createTenants(List<Tenant> tenantList)
    {
        return new Tenants(tenantList);
    }


    public Tenant createTenant(List<Param> params)
    {
        return new Tenant(params);
    }


    public Param createParam(String key, String value)
    {
        return new Param(key, value);
    }
}
