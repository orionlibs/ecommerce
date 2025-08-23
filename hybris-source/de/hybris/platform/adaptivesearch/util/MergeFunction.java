package de.hybris.platform.adaptivesearch.util;

@FunctionalInterface
public interface MergeFunction<K, V>
{
    V apply(K paramK, V paramV1, V paramV2);
}
