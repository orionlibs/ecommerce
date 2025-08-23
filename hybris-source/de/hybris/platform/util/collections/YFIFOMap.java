package de.hybris.platform.util.collections;

public interface YFIFOMap<K, V> extends YMap<K, V>, CacheMap<K, V>
{
    void processRemoveEldest(K paramK, V paramV);
}
