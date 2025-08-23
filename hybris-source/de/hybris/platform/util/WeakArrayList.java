package de.hybris.platform.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WeakArrayList<T> extends AbstractList<T> implements Serializable
{
    private transient List theInnerList = new ArrayList();
    private List<T> theListToSerialize = null;
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
        }
    }


    public boolean add(Object e)
    {
        discard();
        return this.theInnerList.add(new HybrisWeakReference(e, getReferenceQueue()));
    }


    public void add(int index, Object element)
    {
        discard();
        this.theInnerList.add(index, new HybrisWeakReference(element, getReferenceQueue()));
    }


    public boolean addAll(Collection c)
    {
        discard();
        boolean result = false;
        for(Iterator it = c.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            result |= this.theInnerList.add(new HybrisWeakReference(o, getReferenceQueue()));
        }
        return result;
    }


    public boolean addAll(int index, Collection c)
    {
        discard();
        for(Iterator it = c.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            this.theInnerList.add(index++, new HybrisWeakReference(o, getReferenceQueue()));
        }
        return !c.isEmpty();
    }


    public void clear()
    {
        this.theInnerList.clear();
        discard();
    }


    public boolean contains(Object o)
    {
        discard();
        return (!this.theInnerList.isEmpty() && this.theInnerList.contains(new HybrisWeakReference(o)));
    }


    public boolean containsAll(Collection c)
    {
        discard();
        if(!this.theInnerList.isEmpty())
        {
            for(Iterator it = c.iterator(); it.hasNext(); )
            {
                Object o = it.next();
                if(!this.theInnerList.contains(new HybrisWeakReference(o)))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public T get(int index)
    {
        discard();
        HybrisWeakReference ref = this.theInnerList.get(index);
        return (ref != null) ? (T)ref.get() : null;
    }


    public int indexOf(Object o)
    {
        discard();
        return this.theInnerList.isEmpty() ? -1 : this.theInnerList.indexOf(new HybrisWeakReference(o));
    }


    public boolean isEmpty()
    {
        discard();
        return this.theInnerList.isEmpty();
    }


    public Iterator iterator()
    {
        discard();
        return (Iterator)new WeakIterator(this, this.theInnerList);
    }


    public int lastIndexOf(Object o)
    {
        discard();
        return this.theInnerList.isEmpty() ? -1 : this.theInnerList.lastIndexOf(new HybrisWeakReference(o));
    }


    public ListIterator listIterator()
    {
        discard();
        return (ListIterator)new WeakIterator(this, this.theInnerList);
    }


    public ListIterator listIterator(int index)
    {
        discard();
        return (ListIterator)new WeakIterator(this, this.theInnerList, index);
    }


    public boolean remove(Object o)
    {
        discard();
        return this.theInnerList.remove(new HybrisWeakReference(o));
    }


    public T remove(int index)
    {
        discard();
        HybrisWeakReference ref = this.theInnerList.remove(index);
        return (ref != null) ? (T)ref.get() : null;
    }


    public boolean removeAll(Collection c)
    {
        discard();
        boolean result = false;
        for(Iterator it = c.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            result |= this.theInnerList.remove(new HybrisWeakReference(o));
        }
        return result;
    }


    public boolean retainAll(Collection<?> c)
    {
        discard();
        return super.retainAll(c);
    }


    public Object set(int index, Object element)
    {
        discard();
        return this.theInnerList.set(index, new HybrisWeakReference(element, getReferenceQueue()));
    }


    public int size()
    {
        discard();
        return this.theInnerList.size();
    }


    public List subList(int fromIndex, int toIndex)
    {
        discard();
        return super.subList(fromIndex, toIndex);
    }


    public Object[] toArray()
    {
        discard();
        return super.toArray();
    }


    public Object[] toArray(Object[] a)
    {
        discard();
        return super.toArray(a);
    }


    private void writeObject(ObjectOutputStream s) throws IOException
    {
        synchronized(this)
        {
            this.theListToSerialize = new ArrayList<>();
            this.theListToSerialize.addAll(this);
            s.defaultWriteObject();
            this.theListToSerialize.clear();
            this.theListToSerialize = null;
        }
    }


    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        synchronized(this)
        {
            s.defaultReadObject();
            this.theInnerList = new ArrayList();
            addAll(this.theListToSerialize);
            this.theListToSerialize.clear();
            this.theListToSerialize = null;
        }
    }
}
