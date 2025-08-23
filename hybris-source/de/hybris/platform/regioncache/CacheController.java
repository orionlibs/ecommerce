package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import java.util.Collection;

public interface CacheController
{
    <T> T getWithLoader(CacheKey paramCacheKey, CacheValueLoader<T> paramCacheValueLoader) throws CacheValueLoadException;


    <T> T get(CacheKey paramCacheKey);


    void invalidate(CacheKey paramCacheKey);


    Collection<CacheRegion> getRegions();


    void remove(CacheKey paramCacheKey);


    void clearCache(CacheRegion paramCacheRegion);


    void addLifecycleCallback(CacheLifecycleCallback paramCacheLifecycleCallback);


    void removeLifecycleCallback(CacheLifecycleCallback paramCacheLifecycleCallback);


    void setStatsEnabled(boolean paramBoolean);


    void registerTenant(String paramString);
}
