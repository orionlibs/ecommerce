package de.hybris.platform.util.collections;

import java.util.Map;

public interface YMap<K, V> extends Map<K, V>
{
    Map.Entry<K, V> getEntry(K paramK);


    void clear(ClearHandler<K, V> paramClearHandler);
}
