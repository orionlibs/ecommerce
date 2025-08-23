package de.hybris.platform.regioncache.region.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheMap;
import de.hybris.platform.regioncache.region.EvictionCallback;
import java.util.Collection;
import java.util.Map;

public class DefaultCacheMap implements CacheMap
{
    protected final Map<CacheKey, Object> map;
    protected final int maxEntries;
    protected EvictionCallback callback = null;


    public DefaultCacheMap(int maxEntries)
    {
        this.maxEntries = maxEntries;
        this.map = (Map<CacheKey, Object>)new InternalMap(this, maxEntries);
    }


    public void registerEvictionCallback(EvictionCallback callback)
    {
        this.callback = callback;
    }


    public Object get(CacheKey key)
    {
        return this.map.get(key);
    }


    public Object put(CacheKey key, Object value)
    {
        return this.map.put(key, value);
    }


    public Object remove(CacheKey key)
    {
        return this.map.remove(key);
    }


    public boolean contains(CacheKey key)
    {
        return this.map.containsKey(key);
    }


    public long getCacheMaxEntries()
    {
        return this.maxEntries;
    }


    public void clear()
    {
        this.map.clear();
    }


    public long size()
    {
        return this.map.size();
    }


    public Collection<CacheKey> getAllKeys()
    {
        return (Collection<CacheKey>)ImmutableSet.copyOf(this.map.keySet());
    }


    protected void entryRemoved(Map.Entry<CacheKey, Object> eldest)
    {
        if(this.callback != null)
        {
            this.callback.evicted(eldest.getKey(), eldest.getValue());
        }
    }
}
