package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;

public final class CacheFactory
{
    private static final Logger log = Logger.getLogger(CacheFactory.class);
    public static final String REGION_CACHE = "regioncache";
    public static final String CACHE_LEGACY_MODE = "cache.legacymode";


    public Cache createCacheInstance(Tenant tenant)
    {
        if(!isLegacyMode(tenant))
        {
            return createRegionCacheInstance(tenant);
        }
        int cacheSize = tenant.getConfig().getInt("cache.main", 150000);
        if(!tenant.getConfig().getBoolean(Config.Params.BYPASS_HYBRIS_RECOMMENDATIONS, false))
        {
            if(cacheSize > 1000000)
            {
                log.warn("*****************************************************************");
                log.warn("Value '" + cacheSize + "' for property 'cache.main' is bigger than maximal allowed value '1000000'. For more information please contact the hybris support.");
                log.warn("Using maximal allowed value '1000000'.");
                log.warn("*****************************************************************");
                cacheSize = 1000000;
            }
        }
        return (Cache)new DefaultCache(tenant, cacheSize);
    }


    public static boolean isLegacyMode(Tenant tenant)
    {
        return tenant.getConfig().getBoolean("cache.legacymode", false);
    }


    public Cache createCacheInstance(Tenant slaveTenant, Tenant masterTenant)
    {
        if(!isLegacyMode(slaveTenant))
        {
            return createRegionCacheInstance(slaveTenant);
        }
        return (Cache)new DefaultCache(slaveTenant, masterTenant);
    }


    public Cache createCacheInstance(Tenant tenant, int cacheSize)
    {
        if(!isLegacyMode(tenant))
        {
            return createRegionCacheInstance(tenant);
        }
        return (Cache)new DefaultCache(tenant, cacheSize);
    }


    public Cache createCacheInstance(Tenant tenant, int cacheSize, boolean forceExclusiveComputation)
    {
        if(!isLegacyMode(tenant))
        {
            return createRegionCacheInstance(tenant);
        }
        return (Cache)new DefaultCache(tenant, cacheSize, forceExclusiveComputation);
    }


    private Cache createRegionCacheInstance(Tenant tenant)
    {
        return (Cache)new RegionCacheAdapter(tenant, null);
    }
}
