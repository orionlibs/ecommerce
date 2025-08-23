package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SingleCacheRegionResolver implements CacheRegionResolver
{
    protected final CacheRegion region;
    protected final List<CacheRegion> regionCollection;


    @Autowired(required = true)
    public SingleCacheRegionResolver(@Qualifier("cacheRegionProvider") CacheRegionProvider provider)
    {
        if(provider.getAllRegions() == null || provider.getAllRegions().size() != 1)
        {
            throw new IllegalArgumentException("One cache region expected!");
        }
        this.region = provider.getAllRegions().iterator().next();
        this.regionCollection = Collections.singletonList(this.region);
    }


    public CacheRegion resolveForGet(CacheKey key)
    {
        return this.region;
    }


    public List<CacheRegion> resolveForInvalidation(CacheKey key)
    {
        return this.regionCollection;
    }


    public List<CacheRegion> resolveQueryRegions(CacheKey key)
    {
        return this.regionCollection;
    }
}
