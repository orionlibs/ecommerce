package de.hybris.platform.jmx.mbeans;

import java.util.Collection;

public interface MainCacheMBean
{
    Integer getMaximumCacheSize();


    Integer getCurrentCacheSize();


    Integer getCurrentCacheSizeInPercent();


    Integer getMaxReachedCacheSize();


    Integer getMaxReachedCacheSizeInPercent();


    Long getEntitiesGetCount();


    Long getEntitiesAddCount();


    Long getEntitiesRemoveCount();


    Long getEntitiesMissCount();


    Long getEntitiesMissCountInPercent();


    void clearCache();


    String startCacheStatistics();


    String stopCacheStatistics();


    Collection<String> showCacheStatistics(int paramInt1, int paramInt2);
}
