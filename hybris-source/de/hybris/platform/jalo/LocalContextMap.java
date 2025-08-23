package de.hybris.platform.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class LocalContextMap<K, V> extends HashMap<K, V> implements ContextMap<K, V>
{
    private final ContextMap<K, V> parent;
    private volatile Set<K> _attributesWithItems;


    public LocalContextMap(ContextMap parent)
    {
        this(parent, 32);
    }


    public LocalContextMap(ContextMap<K, V> parent, int initialCapacity)
    {
        super(initialCapacity, 0.75F);
        Preconditions.checkNotNull(parent);
        this.parent = parent;
    }


    public int size()
    {
        return keySet().size();
    }


    public boolean isEmpty()
    {
        return !keySet().iterator().hasNext();
    }


    public V get(Object key)
    {
        V ret, value = super.get(key);
        if(value == null)
        {
            ret = (V)this.parent.get(key);
        }
        else if(REMOVED.equals(value) || NULL.equals(value))
        {
            ret = null;
        }
        else
        {
            ret = value;
        }
        return ret;
    }


    public boolean isAttributeHoldingItems(Object key)
    {
        return ((this._attributesWithItems != null && this._attributesWithItems.contains(key)) || this.parent.isAttributeHoldingItems(key));
    }


    public Set<K> getAttributesContainingItems()
    {
        Set<K> ret = (this._attributesWithItems != null) ? new LinkedHashSet<>(this._attributesWithItems) : new LinkedHashSet<>();
        ret.addAll(this.parent.getAttributesContainingItems());
        return ret;
    }


    public boolean containsKey(Object key)
    {
        boolean contains;
        if(super.containsKey(key))
        {
            contains = !REMOVED.equals(super.get(key));
        }
        else
        {
            contains = this.parent.containsKey(key);
        }
        return contains;
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
        super.put(key, (value == null) ? (V)NULL : value);
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


    public void putAll(Map<? extends K, ? extends V> map)
    {
        for(Map.Entry<? extends K, ? extends V> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }


    public V remove(Object key)
    {
        V previousValue;
        if(this.parent.containsKey(key))
        {
            previousValue = put((K)key, (V)REMOVED);
        }
        else
        {
            previousValue = super.remove(key);
        }
        if(this._attributesWithItems != null)
        {
            this._attributesWithItems.remove(key);
        }
        return (NULL.equals(previousValue) || REMOVED.equals(previousValue)) ? null : previousValue;
    }


    public void clear()
    {
        super.clear();
        if(this._attributesWithItems != null)
        {
            this._attributesWithItems.clear();
        }
        for(K key : this.parent.keySet())
        {
            super.put(key, (V)REMOVED);
        }
    }


    public boolean containsValue(Object value)
    {
        Object localValue = (value == null) ? NULL : value;
        for(V entryValue : values())
        {
            if(localValue == entryValue || (entryValue != null && entryValue.equals(localValue)))
            {
                return true;
            }
        }
        return false;
    }


    public Set<K> keySet()
    {
        Set<K> keys = new HashSet<>();
        for(Map.Entry<K, V> entry : entrySet())
        {
            keys.add(entry.getKey());
        }
        return keys;
    }


    public Collection<V> values()
    {
        Set<V> values = new HashSet<>();
        for(Map.Entry<K, V> entry : entrySet())
        {
            values.add(entry.getValue());
        }
        return values;
    }


    public Set<Map.Entry<K, V>> entrySet()
    {
        Set<Map.Entry<K, V>> parentEntrySet = this.parent.entrySet();
        Map<K, V> map = new HashMap<>((int)((2 * parentEntrySet.size()) / 0.75F) + 1);
        for(Map.Entry<K, V> entry : parentEntrySet)
        {
            if(!REMOVED.equals(entry.getValue()))
            {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        for(Map.Entry<K, V> entry : super.entrySet())
        {
            if(REMOVED.equals(entry.getValue()))
            {
                map.remove(entry.getKey());
                continue;
            }
            map.put(entry.getKey(), entry.getValue());
        }
        return map.entrySet();
    }


    public String toString()
    {
        return entrySet().toString();
    }


    public Enumeration<K> keys()
    {
        return Collections.enumeration(keySet());
    }


    public boolean equals(Object o)
    {
        return (super.equals(o) && this.parent.equals(((LocalContextMap)o).parent));
    }


    public int hashCode()
    {
        return super.hashCode() * 31 + this.parent.hashCode();
    }
}
