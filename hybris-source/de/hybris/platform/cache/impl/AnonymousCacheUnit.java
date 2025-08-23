package de.hybris.platform.cache.impl;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;

public class AnonymousCacheUnit extends AbstractCacheUnit
{
    private final Object[] key;


    public AnonymousCacheUnit(Cache cache, Object[] key)
    {
        super(cache);
        this.key = key;
    }


    public Object compute()
    {
        throw new RuntimeException("must not compute AnonymousCacheUnit");
    }


    public final Object[] createKey()
    {
        return this.key;
    }
}
