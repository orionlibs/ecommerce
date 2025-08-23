package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.CacheKey;

public interface CacheValueLoader<V>
{
    V load(CacheKey paramCacheKey) throws CacheValueLoadException;
}
