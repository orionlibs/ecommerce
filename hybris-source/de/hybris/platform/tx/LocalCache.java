package de.hybris.platform.tx;

import de.hybris.platform.cache.AbstractCacheUnit;

interface LocalCache
{
    static LocalCache empty()
    {
        return (LocalCache)EmptyLocalCache.INSTANCE;
    }


    void put(LocalCacheKey paramLocalCacheKey, AbstractCacheUnit paramAbstractCacheUnit);


    AbstractCacheUnit get(LocalCacheKey paramLocalCacheKey);


    void invalidateSingleKey(LocalCacheKey paramLocalCacheKey);


    void clear();


    void remove(LocalCacheKey paramLocalCacheKey);
}
