package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.key.CacheKey;
import java.util.List;

public class DefaultCacheRegionResolver implements CacheRegionResolver
{
    private final CacheRegionProvider cacheRegionProvider;


    public DefaultCacheRegionResolver(CacheRegionProvider cacheRegionProvider)
    {
        this.cacheRegionProvider = cacheRegionProvider;
        if(cacheRegionProvider == null)
        {
            throw new IllegalArgumentException("Region provide cannot be NULL.");
        }
    }


    public CacheRegion resolveForGet(CacheKey key)
    {
        CacheRegion region = null;
        List<CacheRegion> regionGroup = this.cacheRegionProvider.getRegionsForType(key.getTypeCode());
        if(regionGroup == null)
        {
            region = this.cacheRegionProvider.getRegionForAllTypes();
        }
        else if(regionGroup.size() == 1)
        {
            region = regionGroup.get(0);
        }
        return region;
    }


    public List<CacheRegion> resolveForInvalidation(CacheKey key)
    {
        List<CacheRegion> regionGroup = this.cacheRegionProvider.getRegionsForType(key.getTypeCode());
        if(regionGroup == null || regionGroup.size() < 1)
        {
            CacheRegion region = this.cacheRegionProvider.getRegionForAllTypes();
            return (region == null) ? null : this.cacheRegionProvider.getRegionByName(region.getName());
        }
        return regionGroup;
    }


    public List<CacheRegion> resolveQueryRegions(CacheKey key)
    {
        return this.cacheRegionProvider.getQueryRegions();
    }
}
