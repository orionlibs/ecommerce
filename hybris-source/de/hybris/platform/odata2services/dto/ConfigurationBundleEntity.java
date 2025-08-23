package de.hybris.platform.odata2services.dto;

import java.io.Serializable;
import java.util.Set;

public class ConfigurationBundleEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Set<IntegrationObjectBundleEntity> integrationObjectBundles;


    public void setIntegrationObjectBundles(Set<IntegrationObjectBundleEntity> integrationObjectBundles)
    {
        this.integrationObjectBundles = integrationObjectBundles;
    }


    public Set<IntegrationObjectBundleEntity> getIntegrationObjectBundles()
    {
        return this.integrationObjectBundles;
    }
}
