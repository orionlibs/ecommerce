package de.hybris.platform.servicelayer.config.impl;

import de.hybris.platform.core.Tenant;
import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

public class TenantAwareHybrisPropertiesFactory implements FactoryBean<Properties>
{
    private static final String TENANT_ID_IMPLICIT_PROPERTY_KEY = "tenantId";
    private Tenant currentTenant;


    @Required
    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
    }


    public Properties getObject() throws Exception
    {
        Properties props = new Properties();
        props.putAll(this.currentTenant.getConfig().getAllParameters());
        props.put("tenantId", this.currentTenant.getTenantID());
        return props;
    }


    public Class<?> getObjectType()
    {
        return Properties.class;
    }


    public boolean isSingleton()
    {
        return true;
    }
}
