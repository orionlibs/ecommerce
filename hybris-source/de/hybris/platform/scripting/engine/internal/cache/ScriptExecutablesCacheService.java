package de.hybris.platform.scripting.engine.internal.cache;

import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;

public interface ScriptExecutablesCacheService
{
    ScriptDTO putOrGetFromCache(CacheKey paramCacheKey, CacheValueLoader paramCacheValueLoader);


    void invalidate(CacheKey paramCacheKey);
}
