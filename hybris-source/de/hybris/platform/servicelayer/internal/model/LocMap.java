package de.hybris.platform.servicelayer.internal.model;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocMap<K, T> implements Map<K, T>, Serializable
{
    private static final Object NULL_OBJECT = new Object();
    private static final Function<?, ?> VALUE_TRANSFORMER = (Function<?, ?>)new Object();
    private volatile Map<K, T> backingMap = Collections.emptyMap();


    private Map<K, T> ensureMap()
    {
        Map<K, T> backingMap = this.backingMap;
        if(backingMap == Collections.emptyMap())
        {
            synchronized(this)
            {
                backingMap = this.backingMap;
                if(backingMap == Collections.emptyMap())
                {
                    this.backingMap = backingMap = new ConcurrentHashMap<>(16, 0.75F, 4);
                }
            }
        }
        return backingMap;
    }


    public T put(K locale, T value)
    {
        T previousValue = ensureMap().put(locale, (T)getValueOrNullObject(value));
        return (T)getValueOrNullIfNullObject(previousValue);
    }


    public int size()
    {
        Map<K, T> backingMap = this.backingMap;
        return backingMap.size();
    }


    public boolean isEmpty()
    {
        Map<K, T> backingMap = this.backingMap;
        return backingMap.isEmpty();
    }


    public boolean containsKey(Object key)
    {
        Map<K, T> backingMap = this.backingMap;
        return backingMap.containsKey(key);
    }


    public boolean containsValue(Object value)
    {
        Map<K, T> backingMap = this.backingMap;
        return backingMap.containsValue(getValueOrNullObject(value));
    }


    public T get(Object key)
    {
        Map<K, T> backingMap = this.backingMap;
        T value = backingMap.get(key);
        return (T)getValueOrNullIfNullObject(value);
    }


    public T remove(Object key)
    {
        Map<K, T> backingMap = this.backingMap;
        T removedValue = backingMap.remove(key);
        return (T)getValueOrNullIfNullObject(removedValue);
    }


    public void putAll(Map<? extends K, ? extends T> map)
    {
        Map<K, T> backingMap = ensureMap();
        for(Map.Entry<? extends K, ? extends T> entry : map.entrySet())
        {
            backingMap.put(entry.getKey(), entry.getValue());
        }
    }


    public void clear()
    {
        Map<K, T> backingMap = this.backingMap;
        backingMap.clear();
    }


    public Set<K> keySet()
    {
        Map<K, T> backingMap = this.backingMap;
        return backingMap.keySet();
    }


    public Collection<T> values()
    {
        Map<K, T> backingMap = this.backingMap;
        return Maps.transformValues(backingMap, VALUE_TRANSFORMER).values();
    }


    public Set<Map.Entry<K, T>> entrySet()
    {
        Map<K, T> backingMap = this.backingMap;
        return Maps.transformValues(backingMap, VALUE_TRANSFORMER).entrySet();
    }


    private Object getValueOrNullObject(Object value)
    {
        return (value == null) ? NULL_OBJECT : value;
    }


    private Object getValueOrNullIfNullObject(Object value)
    {
        return (value == NULL_OBJECT) ? null : value;
    }


    public String toString()
    {
        return this.backingMap.toString();
    }
}
