package de.hybris.platform.util.collections.fast;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public class YLongSet
{
    private final LongOpenHashSet target;


    public YLongSet()
    {
        this.target = new LongOpenHashSet();
    }


    public YLongSet(int initialCapacity)
    {
        this.target = new LongOpenHashSet(initialCapacity);
    }


    public int size()
    {
        return this.target.size();
    }


    public boolean add(long value)
    {
        return this.target.add(value);
    }


    public boolean remove(long value)
    {
        return this.target.remove(value);
    }


    public boolean isEmpty()
    {
        return this.target.isEmpty();
    }


    public long[] toArray()
    {
        return this.target.toLongArray();
    }


    public boolean contains(long value)
    {
        return this.target.contains(value);
    }


    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof YLongSet))
        {
            return false;
        }
        return this.target.equals(((YLongSet)obj).target);
    }


    public int hashCode()
    {
        return this.target.hashCode();
    }
}
