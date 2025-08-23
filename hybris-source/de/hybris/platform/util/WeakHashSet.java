package de.hybris.platform.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class WeakHashSet implements Set, Serializable
{
    static final Logger log = Logger.getLogger(WeakHashSet.class.getName());
    private transient Set theInnerSet = new HashSet();
    private Set theSetToSerialize = null;
    private transient ReferenceQueue theReferenceQueue = null;


    private ReferenceQueue getReferenceQueue()
    {
        if(this.theReferenceQueue == null)
        {
            this.theReferenceQueue = new ReferenceQueue();
        }
        return this.theReferenceQueue;
    }


    private void discard()
    {
        HybrisWeakReference discarded = null;
        while((discarded = (HybrisWeakReference)getReferenceQueue().poll()) != null)
        {
            remove(discarded);
            log.debug("discarded " + discarded.hashCode());
        }
    }


    public Iterator iterator()
    {
        discard();
        return (Iterator)new WeakIterator(this, this.theInnerSet);
    }


    public int size()
    {
        discard();
        return this.theInnerSet.size();
    }


    public boolean add(Object obj)
    {
        discard();
        return this.theInnerSet.add(new HybrisWeakReference(obj, getReferenceQueue()));
    }


    public boolean addAll(Collection collection)
    {
        discard();
        boolean result = false;
        for(Iterator it = collection.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            result |= this.theInnerSet.add(new HybrisWeakReference(o, getReferenceQueue()));
        }
        return result;
    }


    public void clear()
    {
        this.theInnerSet.clear();
        discard();
    }


    public boolean contains(Object obj)
    {
        discard();
        return this.theInnerSet.contains(new HybrisWeakReference(obj));
    }


    public boolean containsAll(Collection collection)
    {
        discard();
        for(Iterator it = collection.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            if(!this.theInnerSet.contains(new HybrisWeakReference(o)))
            {
                return false;
            }
        }
        return true;
    }


    public boolean isEmpty()
    {
        discard();
        return this.theInnerSet.isEmpty();
    }


    public boolean remove(Object obj)
    {
        discard();
        return this.theInnerSet.remove(new HybrisWeakReference(obj));
    }


    public boolean removeAll(Collection collection)
    {
        discard();
        boolean result = false;
        for(Iterator it = collection.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            result |= this.theInnerSet.remove(new HybrisWeakReference(o));
        }
        return result;
    }


    public boolean retainAll(Collection collection)
    {
        throw new UnsupportedOperationException("Retain is not supported by WeakSet");
    }


    public Object[] toArray()
    {
        discard();
        Object[] array = new Object[size()];
        int pos = 0;
        for(Iterator it = iterator(); it.hasNext(); )
        {
            array[pos++] = it.next();
        }
        return array;
    }


    public Object[] toArray(Object[] obj)
    {
        discard();
        Object[] array = obj;
        if(array.length < size())
        {
            array = (Object[])Array.newInstance(obj.getClass().getComponentType(), size());
        }
        int pos = 0;
        for(Iterator it = iterator(); it.hasNext(); )
        {
            it.next();
            array[pos++] = it.next();
        }
        return array;
    }


    private void writeObject(ObjectOutputStream s) throws IOException
    {
        synchronized(this)
        {
            this.theSetToSerialize = new HashSet();
            this.theSetToSerialize.addAll(this);
            s.defaultWriteObject();
            this.theSetToSerialize.clear();
            this.theSetToSerialize = null;
        }
    }


    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        synchronized(this)
        {
            s.defaultReadObject();
            this.theInnerSet = new HashSet();
            addAll(this.theSetToSerialize);
            this.theSetToSerialize.clear();
            this.theSetToSerialize = null;
        }
    }
}
