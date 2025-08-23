package de.hybris.platform.cache;

public abstract class CustomCacheUnit extends AbstractCacheUnit
{
    private final Object[] key;


    public CustomCacheUnit(Cache cache, String key)
    {
        super(cache);
        this.key = new Object[] {Cache.CACHEKEY_CUSTOM, key};
    }


    public abstract Object compute() throws Exception;


    public Object[] createKey()
    {
        return this.key;
    }
}
