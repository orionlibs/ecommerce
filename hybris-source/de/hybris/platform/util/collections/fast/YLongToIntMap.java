package de.hybris.platform.util.collections.fast;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;

public class YLongToIntMap
{
    private static final int EMPTY_VALUE = 0;
    private final Long2IntOpenHashMap target;


    public YLongToIntMap()
    {
        this.target = new Long2IntOpenHashMap();
        initializeTarget();
    }


    public YLongToIntMap(int initialCapacity)
    {
        this.target = new Long2IntOpenHashMap(initialCapacity);
        initializeTarget();
    }


    public int getEmptyValue()
    {
        return this.target.defaultReturnValue();
    }


    public int put(long key, int value)
    {
        return this.target.put(key, value);
    }


    public int get(long key)
    {
        return this.target.get(key);
    }


    public int remove(long key)
    {
        return this.target.remove(key);
    }


    private void initializeTarget()
    {
        this.target.defaultReturnValue(0);
    }
}
