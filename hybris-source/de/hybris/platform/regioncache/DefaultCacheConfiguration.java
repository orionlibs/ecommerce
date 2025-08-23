package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.invalidation.InvalidationFilter;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultCacheConfiguration implements CacheConfiguration
{
    private final List<CacheRegion> regions;
    private final List<InvalidationFilter> invalidationFilters;
    private final List<CacheRegionResolver> resolvers;


    public DefaultCacheConfiguration(CacheRegionsHolder cacheRegionsHolder, List<InvalidationFilter> invalidationFilters, List<CacheRegionResolver> resolvers)
    {
        this(new ArrayList<>(cacheRegionsHolder.getRegions()), invalidationFilters, resolvers);
    }


    public DefaultCacheConfiguration(List<CacheRegion> regions, List<InvalidationFilter> invalidationFilters, List<CacheRegionResolver> resolvers)
    {
        this.regions = (regions != null) ? new ArrayList<>(regions) : Collections.EMPTY_LIST;
        this.invalidationFilters = (invalidationFilters != null) ? new ArrayList<>(invalidationFilters) : Collections.EMPTY_LIST;
        this.resolvers = (resolvers != null) ? new ArrayList<>(resolvers) : Collections.EMPTY_LIST;
    }


    public List<CacheRegion> getRegions()
    {
        return this.regions;
    }


    public List<InvalidationFilter> getFilters()
    {
        return this.invalidationFilters;
    }


    public List<CacheRegionResolver> getResolvers()
    {
        return this.resolvers;
    }
}
