package de.hybris.platform.regioncache.region.impl;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.googlecode.concurrentlinkedhashmap.Weigher;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Map;

public class LRUCacheRegion extends AbstractMapBasedCacheRegion
{
    public LRUCacheRegion(String name)
    {
        this(name, 2000, true);
    }


    public LRUCacheRegion(String name, int maxEntries, boolean statsEnabled)
    {
        this(name, maxEntries, statsEnabled, null);
    }


    public LRUCacheRegion(String name, int maxEntries, boolean statsEnabled, Weigher<Object> weigher)
    {
        this(name, new CacheStatistics(), statsEnabled, maxEntries, weigher);
    }


    public LRUCacheRegion(String name, CacheStatistics stats, boolean statsEnabled, int maxEntries, Weigher<Object> weigher)
    {
        super(name, stats, statsEnabled);
        Object object = new Object(this, statsEnabled, stats);
        ConcurrentLinkedHashMap.Builder<CacheKey, Object> cacheMapBuilder = (new ConcurrentLinkedHashMap.Builder()).maximumWeightedCapacity(maxEntries).listener((EvictionListener)object);
        if(weigher != null)
        {
            cacheMapBuilder.weigher(weigher);
        }
        this.cacheMap = (Map)cacheMapBuilder.build();
    }


    protected Object putIfAbsent(CacheKey key, Object value)
    {
        return ((ConcurrentLinkedHashMap)this.cacheMap).putIfAbsent(key, value);
    }


    public long getCacheMaxEntries()
    {
        return ((ConcurrentLinkedHashMap)this.cacheMap).capacity();
    }


    private void notifyEvictionListener(CacheKey key, Object value)
    {
        if(this.lifecycleCallback != null)
        {
            this.lifecycleCallback.onAfterEviction(key, value, (CacheRegion)this);
        }
    }
}
