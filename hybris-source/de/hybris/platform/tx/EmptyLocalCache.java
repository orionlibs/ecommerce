package de.hybris.platform.tx;

import de.hybris.platform.cache.AbstractCacheUnit;

class EmptyLocalCache implements LocalCache
{
    static final EmptyLocalCache INSTANCE = new EmptyLocalCache();


    public void put(LocalCacheKey key, AbstractCacheUnit cacheUnit)
    {
    }


    public AbstractCacheUnit get(LocalCacheKey key)
    {
        return null;
    }


    public void invalidateSingleKey(LocalCacheKey key)
    {
    }


    public void clear()
    {
    }


    public void remove(LocalCacheKey key)
    {
    }
}
