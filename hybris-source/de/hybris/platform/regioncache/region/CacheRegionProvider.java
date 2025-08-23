package de.hybris.platform.regioncache.region;

import java.util.Collection;
import java.util.List;

public interface CacheRegionProvider
{
    List<CacheRegion> getAllRegions();


    List<CacheRegion> getRegions();


    List<CacheRegion> getRegionsForType(Object paramObject);


    CacheRegion getRegionForAllTypes();


    List<CacheRegion> getRegionByName(String paramString);


    List<CacheRegion> getQueryRegions();


    CacheRegion getManualRegion(String paramString);


    Collection<CacheRegion> getManualRegions();
}
