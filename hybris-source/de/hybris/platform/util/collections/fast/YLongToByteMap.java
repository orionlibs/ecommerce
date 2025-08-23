package de.hybris.platform.util.collections.fast;

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;

public class YLongToByteMap
{
    private static final byte EMPTY_VALUE = 0;
    private final Long2ByteOpenHashMap target;


    public YLongToByteMap()
    {
        this.target = new Long2ByteOpenHashMap();
        initializeTarget();
    }


    public YLongToByteMap(int initialCapacity)
    {
        this.target = new Long2ByteOpenHashMap(initialCapacity);
        initializeTarget();
    }


    public byte getEmptyValue()
    {
        return this.target.defaultReturnValue();
    }


    public byte put(long key, byte value)
    {
        return this.target.put(key, value);
    }


    public byte get(long key)
    {
        return this.target.get(key);
    }


    private void initializeTarget()
    {
        this.target.defaultReturnValue((byte)0);
    }
}
