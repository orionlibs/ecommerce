package de.hybris.platform.solrfacetsearch.config.cache.impl;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.impl.LRUCacheRegion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchConfigCacheRegion extends LRUCacheRegion
{
    public FacetSearchConfigCacheRegion(String name, int maxEntries, boolean statsEnabled)
    {
        super(name, maxEntries, statsEnabled);
    }


    @Required
    public void setHandledTypes(String[] handledTypes)
    {
        this.handledTypes = Arrays.<String>copyOf(handledTypes, handledTypes.length);
    }


    public List<CacheKey> findCachedObjectKeys(String configName, String tenantId)
    {
        List<CacheKey> cachedObjectKeys = new ArrayList<>();
        for(CacheKey key : this.cacheMap.keySet())
        {
            if(tenantId.equals(key.getTenantId()) && configName.equals(((FacetSearchConfigCacheKey)key).getName()))
            {
                cachedObjectKeys.add(key);
            }
        }
        return cachedObjectKeys;
    }
}
