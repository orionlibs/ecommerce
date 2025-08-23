package de.hybris.platform.cockpit.cache;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestCache<K, V> extends LinkedHashMap
{
    private static final int DEFAULT_CAPACITY = 1024;
    private static final Object NULL_VALUE = new Object();
    private final int capacity;
    private Map.Entry<K, V> latestContainsRequestEntry = new AbstractMap.SimpleEntry<>(null, null);


    public RequestCache()
    {
        this(1024);
    }


    public RequestCache(int capacity)
    {
        super(1024, 0.75F, false);
        this.capacity = capacity;
    }


    protected boolean removeEldestEntry(Map.Entry eldest)
    {
        return (size() > this.capacity);
    }


    public Object get(Object key)
    {
        Object value;
        if(key != null && key.equals(this.latestContainsRequestEntry.getKey()))
        {
            value = this.latestContainsRequestEntry.getValue();
        }
        else
        {
            value = super.get(key);
        }
        return (value == NULL_VALUE) ? null : value;
    }


    public boolean containsKey(Object key)
    {
        Object object = super.get(key);
        if(object != null)
        {
            this.latestContainsRequestEntry = new AbstractMap.SimpleEntry<>((K)key, (V)object);
            return true;
        }
        return false;
    }


    public Object put(Object key, Object value)
    {
        return super.put(key, (value == null) ? NULL_VALUE : value);
    }
}
