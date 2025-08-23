package de.hybris.platform.regioncache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<E> extends AbstractSet<E> implements Serializable
{
    private static final long serialVersionUID = 2L;
    private transient Set<E> target;


    public ConcurrentHashSet()
    {
        this(Collections.EMPTY_LIST);
    }


    public ConcurrentHashSet(Collection<? extends E> collection)
    {
        this(Math.max((int)(collection.size() / 0.75F) + 1, 16));
        addAll(collection);
    }


    public ConcurrentHashSet(int initialCapacity, float loadFactor)
    {
        this(new ConcurrentHashMap<>(initialCapacity, loadFactor, 16));
    }


    public ConcurrentHashSet(int initialCapacity)
    {
        this(new ConcurrentHashMap<>(initialCapacity));
    }


    private ConcurrentHashSet(ConcurrentHashMap<E, Boolean> map)
    {
        this.target = Collections.newSetFromMap(map);
    }


    public Iterator<E> iterator()
    {
        return this.target.iterator();
    }


    public int size()
    {
        return this.target.size();
    }


    public boolean isEmpty()
    {
        return this.target.isEmpty();
    }


    public boolean contains(Object element)
    {
        return this.target.contains(element);
    }


    public boolean add(E element)
    {
        return this.target.add(element);
    }


    public boolean remove(Object element)
    {
        return this.target.remove(element);
    }


    public void clear()
    {
        this.target.clear();
    }


    private void writeObject(ObjectOutputStream stream) throws IOException
    {
        stream.defaultWriteObject();
        stream.writeInt(this.target.size());
        for(Iterator<E> i = this.target.iterator(); i.hasNext(); )
        {
            stream.writeObject(i.next());
        }
    }


    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException
    {
        inputStream.defaultReadObject();
        this.target = Collections.newSetFromMap(new ConcurrentHashMap<>());
        int size = inputStream.readInt();
        for(int i = 0; i < size; i++)
        {
            E object = (E)inputStream.readObject();
            this.target.add(object);
        }
    }
}
