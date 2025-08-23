package de.hybris.platform.util.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDelegatingMap<K, V> implements Map<K, V>, Serializable
{
    protected final Map<K, V> delegateMap = createMapInstance();


    protected abstract Map<K, V> createMapInstance();


    public boolean containsKey(Object key)
    {
        return this.delegateMap.containsKey(key);
    }


    public boolean containsValue(Object value)
    {
        return this.delegateMap.containsValue(value);
    }


    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.delegateMap.entrySet();
    }


    public boolean equals(Object obj)
    {
        return this.delegateMap.equals(obj);
    }


    public int hashCode()
    {
        return this.delegateMap.hashCode();
    }


    public V get(Object key)
    {
        return this.delegateMap.get(key);
    }


    public boolean isEmpty()
    {
        return this.delegateMap.isEmpty();
    }


    public Set<K> keySet()
    {
        return this.delegateMap.keySet();
    }


    public V put(K key, V value)
    {
        return this.delegateMap.put(key, value);
    }


    public void putAll(Map<? extends K, ? extends V> map)
    {
        this.delegateMap.putAll(map);
    }


    public V remove(Object key)
    {
        return this.delegateMap.remove(key);
    }


    public Collection<V> values()
    {
        return this.delegateMap.values();
    }


    public int size()
    {
        return this.delegateMap.size();
    }


    public void clear()
    {
        this.delegateMap.clear();
    }


    public String toString()
    {
        return this.delegateMap.toString();
    }
}
