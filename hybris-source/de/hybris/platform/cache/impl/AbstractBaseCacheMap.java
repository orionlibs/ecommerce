package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.util.collections.AbstractCacheMap;

public abstract class AbstractBaseCacheMap<K> extends AbstractCacheMap<K, AbstractCacheUnit>
{
    private final CacheBase cacheBase;


    public AbstractBaseCacheMap(CacheBase cacheBase, int max)
    {
        super(max);
        this.cacheBase = cacheBase;
    }


    public void processDisplacedEntry(K key, AbstractCacheUnit unit)
    {
        this.cacheBase.removeUnitFromNestedMapOnly(unit);
    }
}
