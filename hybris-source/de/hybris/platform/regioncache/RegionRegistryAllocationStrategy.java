package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.RegionType;

public class RegionRegistryAllocationStrategy
{
    public boolean isRegionRequiresRegistry(CacheRegion region)
    {
        if(region == null)
        {
            return false;
        }
        String[] types = region.getHandledTypes();
        boolean requires = false;
        if(types != null && types.length > 0)
        {
            for(String type : types)
            {
                if(RegionType.NON_REGISTRABLE.value().equals(type))
                {
                    return false;
                }
                if(type == null || type.isEmpty())
                {
                    requires = true;
                }
                if(RegionType.QUERY_CACHE_TYPE.value().equals(type))
                {
                    requires = true;
                }
                if(RegionType.ALL_TYPES.value().equals(type))
                {
                    requires = true;
                }
            }
            return requires;
        }
        return true;
    }
}
