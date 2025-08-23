package de.hybris.platform.adaptivesearch.util;

import java.util.List;
import java.util.Map;

public interface MergeMap<K, V> extends Map<K, V>
{
    int getHighestRank();


    int getLowestRank();


    V mergeBefore(K paramK, V paramV);


    void mergeBefore(MergeMap<K, V> paramMergeMap, MergeFunction<K, V> paramMergeFunction);


    V mergeAfter(K paramK, V paramV);


    void mergeAfter(MergeMap<K, V> paramMergeMap, MergeFunction<K, V> paramMergeFunction);


    List<V> orderedValues();
}
