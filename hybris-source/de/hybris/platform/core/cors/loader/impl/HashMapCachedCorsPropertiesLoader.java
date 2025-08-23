package de.hybris.platform.core.cors.loader.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.cors.loader.CorsPropertiesLoader;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class HashMapCachedCorsPropertiesLoader implements CorsPropertiesLoader, InitializingBean, ConfigIntf.ConfigChangeListener
{
    private CorsPropertiesLoader propertiesLoader;
    private final Map<String, ImmutableMap<String, String>> cachedProperties = new ConcurrentHashMap<>();


    public ImmutableMap<String, String> loadProperties(String contextName)
    {
        ImmutableMap<String, String> result = this.cachedProperties.get(contextName);
        if(result == null)
        {
            result = this.propertiesLoader.loadProperties(contextName);
            this.cachedProperties.put(contextName, result);
        }
        return result;
    }


    public void configChanged(String key, String newValue)
    {
        if(key.startsWith("corsfilter"))
        {
            String mapKey = key.substring("corsfilter".length() + 1);
            int idx = mapKey.indexOf('.');
            if(idx < 0)
            {
                return;
            }
            String cachedPropertiesKey = mapKey.substring(0, idx);
            this.cachedProperties.remove(cachedPropertiesKey);
        }
    }


    public void afterPropertiesSet() throws Exception
    {
        Registry.getCurrentTenantNoFallback().getConfig().registerConfigChangeListener(this);
    }


    @Required
    public void setPropertiesLoader(CorsPropertiesLoader propertiesLoader)
    {
        this.propertiesLoader = propertiesLoader;
    }
}
