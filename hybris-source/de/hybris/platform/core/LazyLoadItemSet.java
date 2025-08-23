package de.hybris.platform.core;

import de.hybris.platform.jalo.Item;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class LazyLoadItemSet<E> implements Set<E>, Serializable
{
    private static final Logger LOG = Logger.getLogger(LazyLoadItemSet.class);
    private boolean modifiable = true;
    private final LazyLoadItemList<E> list;


    public LazyLoadItemSet()
    {
        this(null, Collections.EMPTY_LIST, 100);
    }


    public LazyLoadItemSet(Set<PK> prefetchLanguages, List<PK> pks, int prefetchSize)
    {
        Set<PK> pkSet = new LinkedHashSet<>(pks);
        pkSet.remove(null);
        this.list = new LazyLoadItemList(prefetchLanguages, new ArrayList<>(pkSet), prefetchSize);
    }


    public Iterator<E> iterator()
    {
        if(!isModifiable())
        {
            return (Iterator<E>)new Object(this);
        }
        return this.list.iterator();
    }


    public int size()
    {
        return this.list.size();
    }


    public boolean equals(Object object)
    {
        if(object == this)
        {
            return true;
        }
        if(!(object instanceof Set))
        {
            return false;
        }
        Collection<?> collection = (Collection)object;
        if(collection.size() != size())
        {
            return false;
        }
        try
        {
            return containsAll(collection);
        }
        catch(ClassCastException | NullPointerException e)
        {
            LOG.debug(e);
            return false;
        }
    }


    public int hashCode()
    {
        int hashcode = 0;
        Iterator<E> iterator = iterator();
        while(iterator.hasNext())
        {
            E obj = iterator.next();
            if(obj != null)
            {
                hashcode += obj.hashCode();
            }
        }
        return hashcode;
    }


    public boolean contains(Object object)
    {
        return this.list.contains(object);
    }


    public boolean containsAll(Collection<?> collection)
    {
        return this.list.containsAll(collection);
    }


    public boolean add(E object)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        return (!contains(object) && this.list.add(object));
    }


    public boolean addAll(Collection<? extends E> collection)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        boolean changed = false;
        for(E o : collection)
        {
            changed |= add(o);
        }
        return changed;
    }


    public void clear()
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        this.list.clear();
    }


    public boolean isEmpty()
    {
        return this.list.isEmpty();
    }


    public boolean remove(Object object)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        return this.list.remove(object);
    }


    public boolean removeAll(Collection<?> collection)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        return this.list.removeAll(collection);
    }


    public boolean retainAll(Collection<?> collection)
    {
        if(!isModifiable())
        {
            throw new UnsupportedOperationException("LazyLoadItemSet " + this + " is not modifiable");
        }
        return this.list.retainAll(collection);
    }


    public Object[] toArray()
    {
        return this.list.toArray();
    }


    public <T> T[] toArray(T[] array)
    {
        return (T[])this.list.toArray((Object[])array);
    }


    protected int getPosition(Object object)
    {
        PK pk;
        if(object == null)
        {
            pk = null;
        }
        else if(object instanceof Item)
        {
            pk = ((Item)object).getPK();
        }
        else if(object instanceof PK)
        {
            pk = (PK)object;
        }
        else
        {
            throw new IllegalArgumentException("illegal element type " + object.getClass().getName() + " for LazyLoadItemSet - must be Item or PK");
        }
        int pos = this.list.getPKList().indexOf(pk);
        if(pos < 0)
        {
            throw new IllegalStateException("element " + object + " does not belong to LazyLoadItemSet " + this + " - cannot get position");
        }
        return pos;
    }


    public boolean isModifiable()
    {
        return this.modifiable;
    }


    public void setModifiable(boolean modifiable)
    {
        this.modifiable = modifiable;
    }
}
