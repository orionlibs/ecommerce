package de.hybris.platform.util.collections.fast;

import it.unimi.dsi.fastutil.longs.LongArrayList;

public class YLongList
{
    private final LongArrayList target;


    public YLongList()
    {
        this.target = new LongArrayList();
    }


    public YLongList(int initalCapacity)
    {
        this.target = new LongArrayList(initalCapacity);
    }


    public int size()
    {
        return this.target.size();
    }


    public long get(int offset)
    {
        return this.target.getLong(offset);
    }


    public void add(long value)
    {
        this.target.add(value);
    }
}
