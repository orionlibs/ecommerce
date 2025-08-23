package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Collection;

public interface CacheRegionsHolder
{
    Collection<CacheRegion> getRegions();
}
