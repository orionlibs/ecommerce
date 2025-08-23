package de.hybris.platform.jalo;

import de.hybris.platform.regioncache.ConcurrentHashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class GlobalContextMap<K, V> extends ConcurrentHashMap<K, V> implements ContextMap<K, V>
{
    private volatile Set<K> _attributesWithItems;


    public GlobalContextMap()
    {
        this(32);
    }


    private GlobalContextMap(int initialCapacity)
    {
        super(initialCapacity, 0.75F, 16);
    }


    public V get(Object key)
    {
        V ret, value = super.get(key);
        if(value == null)
        {
            ret = null;
        }
        else if(ContextMap.REMOVED.equals(value) || ContextMap.NULL.equals(value))
        {
            ret = null;
        }
        else
        {
            ret = value;
        }
        return ret;
    }


    public boolean containsKey(Object key)
    {
        return (super.containsKey(key) && !ContextMap.REMOVED.equals(super.get(key)));
    }


    public V put(K key, V value)
    {
        V previousValue = putNoItemCheck(key, value);
        if(valueContainsItems(value))
        {
            if(this._attributesWithItems == null)
            {
                synchronized(this)
                {
                    if(this._attributesWithItems == null)
                    {
                        this._attributesWithItems = (Set<K>)new ConcurrentHashSet(size());
                    }
                }
            }
            this._attributesWithItems.add(key);
        }
        else if(this._attributesWithItems != null)
        {
            this._attributesWithItems.remove(key);
        }
        return previousValue;
    }


    public V putNoItemCheck(K key, V value)
    {
        V previousValue = get(key);
        super.put(key, (value == null) ? (V)ContextMap.NULL : value);
        return previousValue;
    }


    private boolean valueContainsItems(V value)
    {
        if(value != null)
        {
            if(value instanceof Item)
            {
                return true;
            }
            if(value instanceof Collection)
            {
                Collection coll = (Collection)value;
                if(!coll.isEmpty())
                {
                    for(Object o : coll)
                    {
                        if(o instanceof Item)
                        {
                            return true;
                        }
                    }
                }
            }
            else if(value instanceof Map)
            {
                Map map = (Map)value;
                if(!map.isEmpty())
                {
                    for(Map.Entry e : map.entrySet())
                    {
                        if(e.getKey() instanceof Item)
                        {
                            return true;
                        }
                        if(e.getValue() instanceof Item)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public V remove(Object key)
    {
        V previousValue = super.remove(key);
        if(this._attributesWithItems != null)
        {
            this._attributesWithItems.remove(key);
        }
        return (ContextMap.NULL.equals(previousValue) || ContextMap.REMOVED.equals(previousValue)) ? null : previousValue;
    }


    public void clear()
    {
        super.clear();
        if(this._attributesWithItems != null)
        {
            this._attributesWithItems.clear();
        }
    }


    public boolean containsValue(Object value)
    {
        Object localValue = (value == null) ? ContextMap.NULL : value;
        for(Object entryValue : values())
        {
            if(localValue == entryValue || (entryValue != null && entryValue.equals(localValue)))
            {
                return true;
            }
        }
        return false;
    }


    public boolean contains(Object o)
    {
        return containsValue(o);
    }


    public V putIfAbsent(K key, V value)
    {
        throw new UnsupportedOperationException();
    }


    public boolean remove(Object key, Object value)
    {
        throw new UnsupportedOperationException();
    }


    public V replace(K key, V value)
    {
        throw new UnsupportedOperationException();
    }


    public boolean replace(K key, V oldValue, V newValue)
    {
        throw new UnsupportedOperationException();
    }


    public Enumeration<V> elements()
    {
        throw new UnsupportedOperationException();
    }


    public boolean isAttributeHoldingItems(Object key)
    {
        return (this._attributesWithItems != null && this._attributesWithItems.contains(key));
    }


    public Set<K> getAttributesContainingItems()
    {
        return (this._attributesWithItems != null) ? this._attributesWithItems : Collections.EMPTY_SET;
    }
}
