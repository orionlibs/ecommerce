package de.hybris.platform.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeakValueHashMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    private transient Map<K, MyWeakReference<V>> m_map = new HashMap<>();
    private transient ReferenceQueue<MyWeakReference<V>> m_queue = null;
    private Map<K, V> theMapToSerialize = null;


    public int size()
    {
        discard();
        return this.m_map.size();
    }


    private ReferenceQueue<MyWeakReference<V>> getReferenceQueue()
    {
        if(this.m_queue == null)
        {
            this.m_queue = new ReferenceQueue<>();
        }
        return this.m_queue;
    }


    public Set<Map.Entry<K, V>> entrySet()
    {
        return (Set<Map.Entry<K, V>>)new EntrySet(this);
    }


    public boolean containsValue(Object o)
    {
        if(o != null)
        {
            for(Map.Entry<K, MyWeakReference<V>> e : this.m_map.entrySet())
            {
                if(o.equals(((MyWeakReference)e.getValue()).get()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean containsKey(Object p_key)
    {
        discard();
        return this.m_map.containsKey(p_key);
    }


    public V get(Object p_key)
    {
        discard();
        return unref(this.m_map.get(p_key));
    }


    public V put(K key, V p_value)
    {
        discard();
        if(key == null || p_value == null)
        {
            return null;
        }
        MyWeakReference<V> oldRef = this.m_map.put(key, createReference(key, p_value, getReferenceQueue()));
        return unref(oldRef);
    }


    protected MyWeakReference<V> createReference(K key, V value, ReferenceQueue<MyWeakReference<V>> refQueue)
    {
        return new MyWeakReference(value, refQueue, key);
    }


    private V unref(MyWeakReference<V> ref)
    {
        if(ref == null)
        {
            return null;
        }
        return (V)ref.get();
    }


    public V remove(Object p_key)
    {
        return unref(this.m_map.remove(p_key));
    }


    public void clear()
    {
        this.m_map.clear();
        while(getReferenceQueue().poll() != null)
            ;
    }


    public Set keySet()
    {
        discard();
        return this.m_map.keySet();
    }


    public Collection<V> values()
    {
        return (Collection<V>)new ValueCollection(this);
    }


    public boolean isEmpty()
    {
        discard();
        return this.m_map.isEmpty();
    }


    private void discard()
    {
        ReferenceQueue<MyWeakReference<V>> q = getReferenceQueue();
        for(MyWeakReference<V> discarded = (MyWeakReference)q.poll(); discarded != null;
                        discarded = (MyWeakReference)q.poll())
        {
            removeReference(discarded);
        }
    }


    protected void removeReference(MyWeakReference<V> discarded)
    {
        this.m_map.remove(discarded.getKey());
    }


    private void writeObject(ObjectOutputStream s) throws IOException
    {
        synchronized(this)
        {
            this.theMapToSerialize = new HashMap<>(this.m_map.size());
            this.theMapToSerialize.putAll(this);
            s.defaultWriteObject();
            this.theMapToSerialize = null;
        }
    }


    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        synchronized(this)
        {
            s.defaultReadObject();
            this.m_map = new HashMap<>(this.theMapToSerialize.size());
            putAll(this.theMapToSerialize);
            this.theMapToSerialize = null;
        }
    }
}
