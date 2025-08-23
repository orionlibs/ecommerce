package de.hybris.platform.cache2;

public class ObjectCacheManagerImpl<T> extends AbstractObjectCacheManager<T>
{
    public ObjectCacheManagerImpl(ObjectCache<T> cache)
    {
        setCache(cache);
    }
}
