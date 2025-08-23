package de.hybris.platform.servicelayer.internal.service;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import org.springframework.beans.factory.FactoryBean;

public class TenantFactory implements FactoryBean<Tenant>
{
    public Tenant getObject() throws Exception
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant == null)
        {
            throw new IllegalStateException("no tenant active");
        }
        return tenant;
    }


    public Class<?> getObjectType()
    {
        return Tenant.class;
    }


    public boolean isSingleton()
    {
        return false;
    }
}
