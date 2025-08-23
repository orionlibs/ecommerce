package de.hybris.platform.util.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CaseInsensitiveStringMap<V> implements ConcurrentMap<String, V>
{
    private final ThreadLocal<LookupKey> keyForThread = (ThreadLocal<LookupKey>)new Object(this);
    private final ConcurrentMap<Key, V> map;
    private transient Set<Map.Entry<String, V>> entrSet;
    private transient Set<String> keysSet;


    public CaseInsensitiveStringMap()
    {
        this.map = new ConcurrentHashMap<>();
    }


    public CaseInsensitiveStringMap(int initialCapacity)
    {
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }


    public CaseInsensitiveStringMap(int initialCapacity, float loadFactor)
    {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }


    public CaseInsensitiveStringMap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }


    public CaseInsensitiveStringMap(Map<String, V> m)
    {
        if(m == null)
        {
            throw new IllegalArgumentException("map was null");
        }
        this.map = new ConcurrentHashMap<>(Math.max((int)(m.size() / 0.75F) + 1, 16));
        putAll(m);
    }


    private final Key keyForLookup(Object realKey)
    {
        LookupKey key = this.keyForThread.get();
        key.set((String)realKey);
        return (Key)key;
    }


    private final Key keyForPut(String realKey)
    {
        return (Key)new StoredKey(realKey);
    }


    public final V get(Object key)
    {
        return this.map.get(keyForLookup(key));
    }


    public V put(String key, V value)
    {
        return this.map.put(keyForPut(key), value);
    }


    public V remove(Object key)
    {
        return this.map.remove(keyForLookup(key));
    }


    public int size()
    {
        return this.map.size();
    }


    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }


    public boolean containsKey(Object key)
    {
        return this.map.containsKey(keyForLookup(key));
    }


    public boolean containsValue(Object value)
    {
        return this.map.containsValue(value);
    }


    public void putAll(Map<? extends String, ? extends V> m)
    {
        for(Map.Entry<? extends String, ? extends V> e : m.entrySet())
        {
            put(e.getKey(), e.getValue());
        }
    }


    public void clear()
    {
        this.map.clear();
    }


    public Set<String> keySet()
    {
        Set<String> ks = this.keysSet;
        return (ks != null) ? ks : (this.keysSet = (Set<String>)new MyKeySet(this, this.map.keySet()));
    }


    public Collection<V> values()
    {
        return this.map.values();
    }


    public Set<Map.Entry<String, V>> entrySet()
    {
        Set<Map.Entry<String, V>> es = this.entrSet;
        return (es != null) ? es : (this.entrSet = (Set<Map.Entry<String, V>>)new MyEntrySet(this, this.map.entrySet()));
    }


    public V putIfAbsent(String key, V value)
    {
        return this.map.putIfAbsent(keyForPut(key), value);
    }


    public boolean remove(Object key, Object value)
    {
        return this.map.remove(keyForLookup(key), value);
    }


    public boolean replace(String key, V oldValue, V newValue)
    {
        return this.map.replace(keyForLookup(key), oldValue, newValue);
    }


    public V replace(String key, V value)
    {
        return this.map.replace(keyForLookup(key), value);
    }
}
