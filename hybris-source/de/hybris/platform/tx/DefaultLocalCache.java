package de.hybris.platform.tx;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.cache.AbstractCacheUnit;
import java.util.HashMap;

class DefaultLocalCache implements LocalCache
{
    private final HashMap<LocalCacheKey, AbstractCacheUnit> cachedUnits = new HashMap<>();
    private final Multimap<LocalCacheKey, LocalCacheKey> keyToDependingKeysMapping = (Multimap<LocalCacheKey, LocalCacheKey>)HashMultimap.create();


    public void put(LocalCacheKey key, AbstractCacheUnit cacheUnit)
    {
        this.cachedUnits.put(key, cacheUnit);
        for(LocalCacheKey dependantKey : key.getDependantKeys())
        {
            this.keyToDependingKeysMapping.put(dependantKey, key);
        }
    }


    public AbstractCacheUnit get(LocalCacheKey key)
    {
        return this.cachedUnits.get(key);
    }


    public void invalidateSingleKey(LocalCacheKey key)
    {
        invalidateCacheUnit(key);
        for(LocalCacheKey keyToInvalidate : this.keyToDependingKeysMapping.get(key))
        {
            invalidateCacheUnit(keyToInvalidate);
        }
    }


    public void clear()
    {
        this.cachedUnits.clear();
        this.keyToDependingKeysMapping.clear();
    }


    public void remove(LocalCacheKey key)
    {
        this.cachedUnits.remove(key);
    }


    private void invalidateCacheUnit(LocalCacheKey key)
    {
        AbstractCacheUnit unitFromCache = get(key);
        if(unitFromCache != null)
        {
            unitFromCache.executeInvalidation();
        }
    }
}
