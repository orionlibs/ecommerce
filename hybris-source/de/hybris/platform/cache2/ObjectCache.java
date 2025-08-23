package de.hybris.platform.cache2;

public interface ObjectCache<T>
{
    T getObject(ObjectKey<T> paramObjectKey);


    void addObject(ObjectKey<T> paramObjectKey, T paramT);


    T removeObject(ObjectKey<T> paramObjectKey);


    void clear();
}
