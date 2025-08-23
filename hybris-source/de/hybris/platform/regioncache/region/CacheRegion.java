package de.hybris.platform.regioncache.region;

import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Collection;

public interface CacheRegion
{
    Object getWithLoader(CacheKey paramCacheKey, CacheValueLoader paramCacheValueLoader) throws CacheValueLoadException;


    Object get(CacheKey paramCacheKey);


    Object invalidate(CacheKey paramCacheKey, boolean paramBoolean);


    String getName();


    String[] getHandledTypes();


    CacheStatistics getCacheRegionStatistics();


    long getCacheMaxEntries();


    void clearCache();


    void registerLifecycleCallback(CacheLifecycleCallback paramCacheLifecycleCallback);


    long getMaxReachedSize();


    Collection<CacheKey> getAllKeys();


    Object remove(CacheKey paramCacheKey, boolean paramBoolean);


    boolean containsKey(CacheKey paramCacheKey);


    void setStatsEnabled(boolean paramBoolean);


    boolean isStatsEnabled();
}
