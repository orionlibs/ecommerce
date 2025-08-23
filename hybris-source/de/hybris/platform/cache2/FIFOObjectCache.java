package de.hybris.platform.cache2;

import de.hybris.platform.util.collections.YFastFIFOMap;
import de.hybris.platform.util.collections.YFastMap;
import java.util.Map;

public class FIFOObjectCache<T> implements ObjectCache<T>
{
    private Map<Object, T> cache = null;


    public FIFOObjectCache()
    {
        this.cache = (Map<Object, T>)new YFastMap();
    }


    public FIFOObjectCache(int size)
    {
        this.cache = (Map<Object, T>)new YFastFIFOMap(size);
    }


    public void addObject(ObjectKey<T> objectKey, T object)
    {
        this.cache.put(objectKey.getSignature(), object);
    }


    public void clear()
    {
        this.cache.clear();
    }


    public T getObject(ObjectKey<T> objectKey)
    {
        return this.cache.get(objectKey.getSignature());
    }


    public T removeObject(ObjectKey<T> objectKey)
    {
        return this.cache.remove(objectKey.getSignature());
    }
}
