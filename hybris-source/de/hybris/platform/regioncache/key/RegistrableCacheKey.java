package de.hybris.platform.regioncache.key;

public interface RegistrableCacheKey<T> extends CacheKey
{
    T[] getDependentTypes();


    long[] getDependentTypeGenerations();


    void setDependentTypeGenerations(long[] paramArrayOflong);
}
