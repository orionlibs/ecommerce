package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.key.CacheKey;
import java.util.List;

public interface CacheRegionResolver
{
    CacheRegion resolveForGet(CacheKey paramCacheKey);


    List<CacheRegion> resolveForInvalidation(CacheKey paramCacheKey);


    List<CacheRegion> resolveQueryRegions(CacheKey paramCacheKey);
}
