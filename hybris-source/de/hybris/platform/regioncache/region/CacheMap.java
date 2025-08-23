package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Collection;

public interface CacheMap
{
    void registerEvictionCallback(EvictionCallback paramEvictionCallback);


    Object get(CacheKey paramCacheKey);


    Object put(CacheKey paramCacheKey, Object paramObject);


    Object remove(CacheKey paramCacheKey);


    boolean contains(CacheKey paramCacheKey);


    long getCacheMaxEntries();


    void clear();


    long size();


    Collection<CacheKey> getAllKeys();
}
