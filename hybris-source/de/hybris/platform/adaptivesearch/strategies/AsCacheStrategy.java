package de.hybris.platform.adaptivesearch.strategies;

import java.util.function.Function;

public interface AsCacheStrategy
{
    boolean isEnabled(AsCacheScope paramAsCacheScope);


    <V> V getWithLoader(AsCacheKey paramAsCacheKey, Function<AsCacheKey, V> paramFunction);


    void clear();


    long getSize();


    long getHits();


    long getMisses();
}
