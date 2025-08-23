package de.hybris.platform.cache2;

public interface ObjectCacheManager<T>
{
    T fetch(ObjectKey<T> paramObjectKey);


    void invalidate(ObjectKey<T> paramObjectKey);


    ObjectCache<T> getCache();


    void setCache(ObjectCache<T> paramObjectCache);
}
