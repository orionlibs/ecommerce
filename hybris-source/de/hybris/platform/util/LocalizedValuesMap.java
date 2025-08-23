package de.hybris.platform.util;

import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class LocalizedValuesMap<T> implements Map<Language, T>
{
    private int pos = 0;
    private final Object[][] data;
    private Set<Map.Entry<Language, T>> entrySetVariable;
    private Set<Language> keySetVariable;
    private Collection<T> valuesCollection;


    public LocalizedValuesMap(int fixedSize)
    {
        this.data = new Object[fixedSize][1];
    }


    public void clear()
    {
        throw new UnsupportedOperationException();
    }


    protected int getKeyPos(Language key)
    {
        for(int i = 0; i < this.data.length; i++)
        {
            if(key == this.data[i][0] || (key != null && key.equals(this.data[i][0])))
            {
                return i;
            }
        }
        return -1;
    }


    public boolean containsKey(Object key)
    {
        return (key instanceof Language && getKeyPos((Language)key) > -1);
    }


    public boolean containsValue(Object value)
    {
        for(int i = 0; i < this.data.length; i++)
        {
            if(value == this.data[i][1] || (value != null && value.equals(this.data[i][1])))
            {
                return true;
            }
        }
        return false;
    }


    public Set<Map.Entry<Language, T>> entrySet()
    {
        if(this.entrySetVariable == null)
        {
            this.entrySetVariable = (Set<Map.Entry<Language, T>>)new LocalizedValuesMapEntrySet(this);
        }
        return this.entrySetVariable;
    }


    public T get(Object key)
    {
        int pos = (key instanceof Language) ? getKeyPos((Language)key) : -1;
        return (pos > -1) ? (T)this.data[pos][1] : null;
    }


    public boolean isEmpty()
    {
        return (this.data.length == 0);
    }


    public Set<Language> keySet()
    {
        if(this.keySetVariable == null)
        {
            this.keySetVariable = (Set<Language>)new KeyAndValuesSet(this, true);
        }
        return this.keySetVariable;
    }


    public T put(Language key, T value)
    {
        int currentPos = getKeyPos(key);
        if(currentPos > -1)
        {
            Object ret = this.data[currentPos][1];
            this.data[currentPos][1] = value;
            return (T)ret;
        }
        if(this.pos < this.data.length)
        {
            this.data[this.pos][0] = key;
            this.data[this.pos][1] = value;
            this.pos++;
            return null;
        }
        throw new IllegalStateException("LocalizedValuesMap already filled");
    }


    public void putAll(Map<? extends Language, ? extends T> t)
    {
        for(Map.Entry<? extends Language, ? extends T> e : t.entrySet())
        {
            put(e.getKey(), e.getValue());
        }
    }


    public T remove(Object key)
    {
        throw new UnsupportedOperationException();
    }


    public int size()
    {
        return this.data.length;
    }


    public Collection<T> values()
    {
        if(this.valuesCollection == null)
        {
            this.valuesCollection = (Collection<T>)new KeyAndValuesSet(this, false);
        }
        return this.valuesCollection;
    }
}
