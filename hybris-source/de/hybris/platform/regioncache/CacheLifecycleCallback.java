package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;

public interface CacheLifecycleCallback
{
    void onAfterAdd(CacheKey paramCacheKey, Object paramObject, CacheRegion paramCacheRegion);


    void onAfterRemove(CacheKey paramCacheKey, Object paramObject, CacheRegion paramCacheRegion);


    void onAfterEviction(CacheKey paramCacheKey, Object paramObject, CacheRegion paramCacheRegion);


    void onMissLoad(CacheKey paramCacheKey, Object paramObject, CacheRegion paramCacheRegion);
}
