package de.hybris.platform.util.collections;

public abstract class AbstractCacheMap<K, V> extends AbstractDelegatingMap<K, V> implements CacheMap<K, V>
{
    private final int max;


    public AbstractCacheMap(int max)
    {
        this.max = max;
    }


    public void clear()
    {
        this.delegateMap.clear();
    }


    public int maxSize()
    {
        return this.max;
    }


    public int getMaxReachedSize()
    {
        return 0;
    }


    public void processDisplacedEntry(K key, V value)
    {
    }
}
