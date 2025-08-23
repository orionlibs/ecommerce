package de.hybris.platform.util.collections.fast;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;

public class YLongToLongMap
{
    private static final long EMPTY_VALUE = 0L;
    private final Long2LongOpenHashMap target;


    public YLongToLongMap()
    {
        this.target = new Long2LongOpenHashMap();
        initializeTarget();
    }


    public long getEmptyValue()
    {
        return this.target.defaultReturnValue();
    }


    public long put(long key, long value)
    {
        return this.target.put(key, value);
    }


    public long get(long key)
    {
        return this.target.get(key);
    }


    public boolean containsKey(long key)
    {
        return this.target.containsKey(key);
    }


    private void initializeTarget()
    {
        this.target.defaultReturnValue(0L);
    }
}
