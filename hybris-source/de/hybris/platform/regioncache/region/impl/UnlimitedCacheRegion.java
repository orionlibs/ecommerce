package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.concurrent.ConcurrentHashMap;

public class UnlimitedCacheRegion extends AbstractMapBasedCacheRegion
{
    public UnlimitedCacheRegion(String name)
    {
        this(name, new CacheStatistics(), true);
    }


    public UnlimitedCacheRegion(String name, boolean statsEnabled)
    {
        this(name, new CacheStatistics(), statsEnabled);
    }


    public UnlimitedCacheRegion(String name, CacheStatistics stats, boolean statsEnabled)
    {
        super(name, stats, statsEnabled);
        this.cacheMap = new ConcurrentHashMap<>(20000);
    }


    protected Object putIfAbsent(CacheKey key, Object value)
    {
        return ((ConcurrentHashMap<CacheKey, Object>)this.cacheMap).putIfAbsent(key, value);
    }


    public long getCacheMaxEntries()
    {
        return 0L;
    }
}
