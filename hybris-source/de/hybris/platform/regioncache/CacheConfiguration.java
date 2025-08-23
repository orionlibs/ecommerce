package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.invalidation.InvalidationFilter;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionResolver;
import java.util.List;

public interface CacheConfiguration
{
    List<CacheRegion> getRegions();


    List<InvalidationFilter> getFilters();


    List<CacheRegionResolver> getResolvers();
}
