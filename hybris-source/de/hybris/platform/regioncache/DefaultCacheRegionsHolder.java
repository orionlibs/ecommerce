package de.hybris.platform.regioncache;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Collection;
import java.util.LinkedHashSet;

public class DefaultCacheRegionsHolder implements CacheRegionsHolder
{
    private final Collection<CacheRegion> regions;


    public DefaultCacheRegionsHolder(Collection<CacheRegion>... regionsToMerge)
    {
        this.regions = merge(regionsToMerge);
    }


    public Collection<CacheRegion> getRegions()
    {
        return this.regions;
    }


    private static Collection<CacheRegion> merge(Collection<CacheRegion>... regionsToMerge)
    {
        Collection<CacheRegion> result = new LinkedHashSet<>();
        for(Collection<CacheRegion> regions : regionsToMerge)
        {
            result.addAll(regions);
        }
        return (Collection<CacheRegion>)ImmutableList.copyOf(result);
    }
}
