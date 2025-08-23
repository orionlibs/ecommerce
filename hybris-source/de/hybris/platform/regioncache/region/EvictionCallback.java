package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.key.CacheKey;

public interface EvictionCallback
{
    void evicted(CacheKey paramCacheKey, Object paramObject);
}
