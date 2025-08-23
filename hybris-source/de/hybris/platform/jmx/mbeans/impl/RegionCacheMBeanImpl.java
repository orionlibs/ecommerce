package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.jmx.mbeans.RegionCacheMBean;
import de.hybris.platform.regioncache.region.CacheRegion;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Overview of one of the region caches of the hybris platform.")
public class RegionCacheMBeanImpl extends AbstractJMXMBean implements RegionCacheMBean
{
    private CacheRegion cacheRegion;


    @ManagedAttribute(description = "The maximum number of cache entries.")
    public Long getMaximumCacheSize()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current amount of entries.")
    public Long getCurrentCacheSize()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current size of the cache in percent, related to maximumCacheSize.")
    public Integer getCacheFillRatio()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities not existing in cache that were requested since a last clear.")
    public Long getMissCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of items existing in cache.")
    public Long getHitCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of items which were requested that were in the cache in percent.")
    public Integer getHitRatio()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of items existing in cache.")
    public Long getFetchCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of items invalidated in cache.")
    public Long getInvalidationCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of items evicted from the cache.")
    public Long getEvictionCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @Required
    public void setCacheRegion(CacheRegion cacheRegion)
    {
        this.cacheRegion = cacheRegion;
    }
}
