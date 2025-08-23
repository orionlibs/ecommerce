package de.hybris.platform.core.cors.loader.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.cors.loader.CorsPropertiesLoader;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class GuavaCachedCorsPropertiesLoader implements CorsPropertiesLoader, InitializingBean, ConfigIntf.ConfigChangeListener
{
    private static final Logger LOG = Logger.getLogger(GuavaCachedCorsPropertiesLoader.class);
    private CorsPropertiesLoader propertiesLoader;
    private final CacheLoader<String, ImmutableMap<String, String>> cacheLoader = (CacheLoader<String, ImmutableMap<String, String>>)new Object(this);
    private final LoadingCache<String, ImmutableMap<String, String>> cachedProperties = CacheBuilder.newBuilder()
                    .build(this.cacheLoader);


    public ImmutableMap<String, String> loadProperties(String contextName)
    {
        try
        {
            return (ImmutableMap<String, String>)this.cachedProperties.get(contextName);
        }
        catch(ExecutionException exception)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(exception.getMessage());
            }
            return this.propertiesLoader.loadProperties(contextName);
        }
    }


    public void configChanged(String key, String newValue)
    {
        if(key.startsWith("corsfilter"))
        {
            String mapKey = key.substring("corsfilter".length() + 1);
            if(mapKey.contains("."))
            {
                this.cachedProperties.invalidate(mapKey.substring(0, mapKey.indexOf('.')));
            }
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
