package de.hybris.platform.servicelayer.config.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.util.config.RuntimeConfigUpdateListener;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConfigurationService extends AbstractService implements ConfigurationService
{
    private Configuration cfg;
    private TenantService tenantService;


    public Configuration getConfiguration()
    {
        return this.cfg;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    public void afterPropertiesSet() throws Exception
    {
        super.afterPropertiesSet();
        this.cfg = (Configuration)new HybrisConfiguration(Registry.getTenantByID(this.tenantService.getCurrentTenantId()).getConfig());
        Registry.registerTenantListener((TenantListener)new RuntimeConfigUpdateListener(getCurrentTenant()));
    }
}
