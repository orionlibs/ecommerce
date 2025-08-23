package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.TenantInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class StandalonePropertiesLoader implements PropertiesLoader
{
    private Properties properties = null;


    public StandalonePropertiesLoader(PlatformConfig platformConfig, String tenantId)
    {
        this.properties = new Properties();
        ConfigUtil.loadRuntimeProperties(this.properties, platformConfig);
        if(tenantId != null && !"master".equalsIgnoreCase(tenantId))
        {
            Map<String, TenantInfo> tenants = platformConfig.getTenantInfos();
            TenantInfo info = tenants.get(tenantId);
            if(info == null)
            {
                throw new IllegalArgumentException("Invalid tenant id '" + tenantId + "' - cannot find any configuration!");
            }
            this.properties.putAll(info.getTenantProperties());
        }
    }


    public String getProperty(String key)
    {
        return this.properties.getProperty(key);
    }


    public String getProperty(String key, String defaultValue)
    {
        return this.properties.getProperty(key, defaultValue);
    }


    public Map<String, String> getAllProperties()
    {
        Set<String> propertyNames = this.properties.stringPropertyNames();
        Map<String, String> retMap = new HashMap<>();
        for(String propertyName : propertyNames)
        {
            retMap.put(propertyName, this.properties.getProperty(propertyName));
        }
        return retMap;
    }
}
