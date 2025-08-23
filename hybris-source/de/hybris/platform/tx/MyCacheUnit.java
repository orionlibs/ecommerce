package de.hybris.platform.tx;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;

class MyCacheUnit extends AbstractCacheUnit
{
    final Object[] key;
    private final String initialValue;
    private int computeCount = 0;


    public MyCacheUnit(Cache cache, String initialValue, Object... key)
    {
        super(cache);
        this.initialValue = initialValue;
        this.key = key;
    }


    public int getInvalidationTopicDepth()
    {
        return this.key.length - 1;
    }


    public Object[] createKey()
    {
        return this.key;
    }


    public String compute() throws Exception
    {
        return this.initialValue + this.initialValue;
    }
}
