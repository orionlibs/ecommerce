package de.hybris.platform.cache;

import de.hybris.platform.util.collections.YFastFIFOMap;

public class BaseCacheYFastFIFOMapStub<K> extends YFastFIFOMap<K, AbstractCacheUnit>
{
    private final CacheBaseStub cacheBase;


    public BaseCacheYFastFIFOMapStub(CacheBaseStub cacheBase, int max)
    {
        super(max);
        this.cacheBase = cacheBase;
    }


    public void processDisplacedEntry(Object key, AbstractCacheUnit value)
    {
        this.cacheBase.removeUnitFromNestedMapOnly(value);
    }
}
