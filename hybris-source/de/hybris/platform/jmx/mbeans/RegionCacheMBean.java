package de.hybris.platform.jmx.mbeans;

public interface RegionCacheMBean
{
    Long getMaximumCacheSize();


    Long getCurrentCacheSize();


    Integer getCacheFillRatio();


    Long getMissCount();


    Long getHitCount();


    Long getFetchCount();


    Long getInvalidationCount();


    Long getEvictionCount();


    Integer getHitRatio();
}
