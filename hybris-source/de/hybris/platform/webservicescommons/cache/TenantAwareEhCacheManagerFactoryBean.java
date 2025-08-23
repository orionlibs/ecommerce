package de.hybris.platform.webservicescommons.cache;

import de.hybris.platform.core.Registry;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

public class TenantAwareEhCacheManagerFactoryBean extends EhCacheManagerFactoryBean
{
    public static final String CACHE_SUFFIX_PROPERTY = "webservicescommons.cacheSuffix";
    private String cacheNamePrefix = "wsCache_";


    public void afterPropertiesSet()
    {
        String cacheName = this.cacheNamePrefix + this.cacheNamePrefix + Registry.getCurrentTenant().getTenantID();
        setCacheManagerName(cacheName);
        super.afterPropertiesSet();
    }


    public void setCacheNamePrefix(String cacheNamePrefix)
    {
        this.cacheNamePrefix = cacheNamePrefix;
    }
}
