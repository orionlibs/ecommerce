package de.hybris.platform.util.collections;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentWeakHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>
{
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    final int segmentMask;
    final int segmentShift;
    final WeakHashMap<K, V>[] segments;
    transient Set<K> keySetField;


    private static int hash(Object o)
    {
        if(o == null)
        {
            return 0;
        }
        int hashCode = o.hashCode();
        hashCode ^= hashCode << 7;
        hashCode ^= hashCode >>> 3;
        hashCode ^= hashCode << 27;
        hashCode ^= hashCode >>> 15;
        return hashCode;
    }


    private final WeakHashMap<K, V> segmentFor(int hash)
    {
        return this.segments[hash >>> this.segmentShift & this.segmentMask];
    }


    public ConcurrentWeakHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        if(initialCapacity < 0)
        {
            throw new IllegalArgumentException("Illegal Initial Capacity: " + initialCapacity);
        }
        if(loadFactor >= 1.0F || loadFactor <= 0.0F)
        {
            throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
        }
        if(concurrencyLevel <= 1)
        {
            throw new IllegalArgumentException("Illegal concurrencyLevel: " + concurrencyLevel);
        }
        if(concurrencyLevel > 65536)
        {
            concurrencyLevel = 65536;
        }
        int sshift = 0;
        int ssize = 1;
        while(ssize < concurrencyLevel)
        {
            sshift++;
            ssize <<= 1;
        }
        this.segmentShift = 32 - sshift;
        this.segmentMask = ssize - 1;
        this.segments = (WeakHashMap<K, V>[])new WeakHashMap[ssize];
        if(initialCapacity > 1073741824)
        {
            initialCapacity = 1073741824;
        }
        int c = initialCapacity / ssize;
        if(c * ssize < initialCapacity)
        {
            c++;
        }
        int cap = 1;
        while(cap < c)
        {
            cap <<= 1;
        }
        for(int i = 0; i < this.segments.length; i++)
        {
            this.segments[i] = new WeakHashMap<>(cap, loadFactor);
        }
    }


    public ConcurrentWeakHashMap(int initialCapacity)
    {
        this(initialCapacity, 0.75F, 16);
    }


    public ConcurrentWeakHashMap()
    {
        this(32, 0.75F, 16);
    }


    public V put(K key, V value)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            return whm.put(key, value);
        }
    }


    public V get(Object key)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        return whm.get(key);
    }


    public V remove(Object key)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            return whm.remove(key);
        }
    }


    public boolean remove(Object key, Object value)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            if(whm.containsKey(key) && whm.get(key).equals(value))
            {
                whm.remove(key);
                return true;
            }
        }
        return false;
    }


    public int size()
    {
        int sum = 0;
        for(int i = 0; i < this.segments.length; i++)
        {
            sum += this.segments[i].size();
        }
        return sum;
    }


    public boolean containsKey(Object key)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            return whm.containsKey(key);
        }
    }


    public void clear()
    {
        for(int i = 0; i < this.segments.length; i++)
        {
            WeakHashMap<K, V> whm = this.segments[i];
            synchronized(whm)
            {
                whm.clear();
            }
        }
    }


    public Set<K> keySet()
    {
        if(this.keySetField == null)
        {
            this.keySetField = (Set<K>)new Object(this);
        }
        return this.keySetField;
    }


    public V getOrPut(K key, LazyValueCreator<K, V> valueCreator)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            V ret = whm.get(key);
            if(!whm.containsKey(key))
            {
                V value = (V)valueCreator.createValue(key);
                whm.put(key, value);
                ret = value;
            }
            return ret;
        }
    }


    public V putIfAbsent(K key, V value)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            if(!whm.containsKey(key))
            {
                return whm.put(key, value);
            }
            return whm.get(key);
        }
    }


    public boolean replace(K key, V oldValue, V newValue)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            if(whm.containsKey(key) && whm.get(key).equals(oldValue))
            {
                whm.put(key, newValue);
                return true;
            }
        }
        return false;
    }


    public V replace(K key, V value)
    {
        int hash = hash(key);
        WeakHashMap<K, V> whm = segmentFor(hash);
        synchronized(whm)
        {
            if(whm.containsKey(key))
            {
                return whm.put(key, value);
            }
        }
        return null;
    }


    public Set<Map.Entry<K, V>> entrySet()
    {
        return null;
    }
}
