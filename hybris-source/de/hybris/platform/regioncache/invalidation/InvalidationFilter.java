package de.hybris.platform.regioncache.invalidation;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;

public interface InvalidationFilter
{
    boolean allowInvalidation(CacheKey paramCacheKey, CacheRegion paramCacheRegion);
}
