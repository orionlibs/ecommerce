package de.hybris.platform.util.collections;

import java.util.Map;

public class YFastFIFOMap<K, V> extends YFastMap<K, V> implements YFIFOMap<K, V>
{
    private final int maxEntries;
    private Map internalMap;


    public YFastFIFOMap(int maxEntries)
    {
        this.maxEntries = maxEntries;
    }


    private int getMaxEntries()
    {
        return this.maxEntries;
    }


    protected Map createMapInstance()
    {
        this.internalMap = (Map)new Object(this);
        return this.internalMap;
    }


    public void processRemoveEldest(K key, V value)
    {
        processDisplacedEntry(key, value);
    }


    public int getMaxReachedSize()
    {
        return super.getMaxReachedSize();
    }


    public int maxSize()
    {
        return this.maxEntries;
    }


    public void processDisplacedEntry(K key, V value)
    {
    }


    public boolean synchronizeExternal()
    {
        return false;
    }
}
