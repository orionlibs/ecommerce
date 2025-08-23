package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.util.collections.YFastFIFOMap;

public class BaseCacheYFastFIFOMap<K> extends YFastFIFOMap<K, AbstractCacheUnit>
{
    private final CacheBase cacheBase;


    public BaseCacheYFastFIFOMap(CacheBase cacheBase, int max)
    {
        super(max);
        this.cacheBase = cacheBase;
    }


    public void processDisplacedEntry(Object key, AbstractCacheUnit value)
    {
        this.cacheBase.removeUnitFromNestedMapOnly(value);
    }
}
