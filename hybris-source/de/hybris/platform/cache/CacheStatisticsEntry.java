package de.hybris.platform.cache;

public interface CacheStatisticsEntry
{
    String getKeyString();


    long getHitCount();


    long getMissCount();


    int getFactor();
}
