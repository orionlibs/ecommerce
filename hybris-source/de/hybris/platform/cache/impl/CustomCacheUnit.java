package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;

public abstract class CustomCacheUnit extends AbstractCacheUnit
{
    private final Object[] key;


    public CustomCacheUnit(Cache cache, String key)
    {
        super(cache);
        this.key = new Object[] {DefaultCache.CACHEKEY_CUSTOM, key};
    }


    public abstract Object compute() throws Exception;


    public Object[] createKey()
    {
        return this.key;
    }
}
