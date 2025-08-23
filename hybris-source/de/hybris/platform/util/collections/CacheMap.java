package de.hybris.platform.util.collections;

import java.util.Map;

public interface CacheMap<K, V> extends Map<K, V>
{
    void clear();


    int size();


    int maxSize();


    int getMaxReachedSize();


    void processDisplacedEntry(K paramK, V paramV);
}
