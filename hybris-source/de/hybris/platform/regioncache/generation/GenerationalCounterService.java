package de.hybris.platform.regioncache.generation;

import java.util.Map;

public interface GenerationalCounterService<KEY>
{
    long[] getGenerations(KEY[] paramArrayOfKEY, String paramString);


    Map<KEY, Long> getGenerations(String paramString);


    void incrementGeneration(KEY paramKEY, String paramString);


    void clear();
}
