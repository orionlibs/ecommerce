package de.hybris.platform.cache2;

import de.hybris.platform.util.collections.YFastMap;
import java.util.Map;

public class SimpleObjectCache<T> implements ObjectCache<T>
{
    private final Map<Object, T> cacheMap = (Map<Object, T>)new YFastMap();


    public T getObject(ObjectKey<T> objectKey)
    {
        return this.cacheMap.get(objectKey.getSignature());
    }


    public void addObject(ObjectKey<T> objectKey, T object)
    {
        this.cacheMap.put(objectKey.getSignature(), object);
    }


    public T removeObject(ObjectKey<T> objectKey)
    {
        return this.cacheMap.remove(objectKey.getSignature());
    }


    public void clear()
    {
        this.cacheMap.clear();
    }
}
