/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Standard map with additional possibilities of {@link com.hybris.cockpitng.core.model.Observable}
 */
public class ObservableMap<V> extends AbstractObservable implements Map<String, V>
{
    private final Map<String, V> values;


    public ObservableMap()
    {
        values = new HashMap<>();
    }


    public ObservableMap(final Class<? extends Map> mapClass) throws IllegalAccessException, InstantiationException
    {
        values = mapClass.newInstance();
    }


    @Override
    public int size()
    {
        return values.size();
    }


    @Override
    public boolean isEmpty()
    {
        return values.isEmpty();
    }


    @Override
    public boolean containsKey(final Object key)
    {
        return values.containsKey(key);
    }


    @Override
    public boolean containsValue(final Object value)
    {
        return values.containsValue(value);
    }


    @Override
    public V get(final Object key)
    {
        return values.get(key);
    }


    @Override
    public V put(final String key, final V value)
    {
        final V result = values.put(key, value);
        changed(key);
        return result;
    }


    @Override
    public V remove(final Object key)
    {
        final V result = values.remove(key);
        changed(ObjectUtils.toString(key));
        return result;
    }


    @Override
    public void putAll(final Map<? extends String, ? extends V> m)
    {
        values.putAll(m);
        m.keySet().forEach(this::changed);
    }


    @Override
    public void clear()
    {
        values.clear();
        changed();
    }


    @Override
    public Set<String> keySet()
    {
        return new KeySet();
    }


    @Override
    public Collection<V> values()
    {
        return new Values();
    }


    @Override
    public Set<Entry<String, V>> entrySet()
    {
        return new EntrySet();
    }


    private abstract class AbstractIterator<I> implements Iterator<I>
    {
        private final Iterator<Entry<String, V>> iterator = values.entrySet().iterator();
        private Entry<String, V> current;


        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }


        protected abstract I next(Entry<String, V> entry);


        @Override
        public I next()
        {
            current = iterator.next();
            return next(current);
        }


        @Override
        public void remove()
        {
            iterator.remove();
            changed(current.getKey());
        }
    }


    private class EntrySet extends AbstractCollection<Entry<String, V>> implements Set<Entry<String, V>>
    {
        private final Set<Entry<String, V>> entries = values.entrySet();


        @Override
        public Iterator<Entry<String, V>> iterator()
        {
            return new EntryIterator();
        }


        @Override
        public int size()
        {
            return entries.size();
        }
    }


    private class EntryIterator extends AbstractIterator<Entry<String, V>>
    {
        @Override
        protected Entry<String, V> next(final Entry<String, V> entry)
        {
            return entry;
        }
    }


    private class KeySet extends AbstractCollection<String> implements Set<String>
    {
        private final Set<String> keys = values.keySet();


        @Override
        public Iterator<String> iterator()
        {
            return new KeyIterator();
        }


        @Override
        public int size()
        {
            return keys.size();
        }
    }


    private class KeyIterator extends AbstractIterator<String>
    {
        @Override
        protected String next(final Entry<String, V> entry)
        {
            return entry.getKey();
        }
    }


    private class Values extends AbstractCollection<V>
    {
        private final Collection<V> values = ObservableMap.this.values.values();


        @Override
        public Iterator<V> iterator()
        {
            return new ValueIterator();
        }


        @Override
        public int size()
        {
            return values.size();
        }
    }


    private class ValueIterator extends AbstractIterator<V>
    {
        @Override
        protected V next(final Entry<String, V> entry)
        {
            return entry.getValue();
        }
    }
}
