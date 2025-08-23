package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.jmx.mbeans.MainCacheMBean;
import java.util.Collection;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Overview of the main cache of the hybris platform.")
public class MainCacheMBeanImpl extends AbstractJMXMBean implements MainCacheMBean
{
    @ManagedAttribute(description = "The maximum number of cache entries. When it is exceeded, the cache mechanism removes the least recently used entries before adding new ones.")
    public Integer getMaximumCacheSize()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current amount of entries in internal cache.")
    public Integer getCurrentCacheSize()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Current size of the internal cache in percent, related to maximumCacheSize.")
    public Integer getCurrentCacheSizeInPercent()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The maximum number of cache entries reached since the cache creation and reseted when clearCache is called.")
    public Integer getMaxReachedCacheSize()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The maximum number of cache entries in percent (related to maximumCacheSize), reached since the cache creation or a last clear.")
    public Integer getMaxReachedCacheSizeInPercent()
    {
        return (Integer)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities requested from the cache since its creation or a last clear.")
    public Long getEntitiesGetCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities added to the cache since creation or last clear.")
    public Long getEntitiesAddCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities removed from the cache since its creation or the last clear.")
    public Long getEntitiesRemoveCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities not existing in cache that were requested since a last clear.")
    public Long getEntitiesMissCount()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The number of entities which were requested but wasn't in the cache in percent.")
    public Long getEntitiesMissCountInPercent()
    {
        return (Long)(new Object(this))
                        .getResult();
    }


    @ManagedOperation(description = "Clears the internal cache and the cache statistics.")
    public void clearCache()
    {
        new Object(this);
    }


    @ManagedOperation(description = "Starts collecting data for the cache statistics.")
    public String startCacheStatistics()
    {
        return (String)(new Object(this))
                        .getResult();
    }


    @ManagedOperation(description = "Stops collecting data for the cache statistics.")
    public String stopCacheStatistics()
    {
        return (String)(new Object(this))
                        .getResult();
    }


    @ManagedOperation(description = "Shows the cache statistics.")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "lowerBound", description = "lower key ratio in percent"), @ManagedOperationParameter(name = "upperBound", description = "upper key ratio in percent")})
    public Collection<String> showCacheStatistics(int lowerBound, int upperBound)
    {
        int internalLowerBound = (lowerBound < 0) ? 0 : ((lowerBound > 100) ? 100 : lowerBound);
        int internalUpperBound = (upperBound > 100) ? 100 : ((upperBound < lowerBound) ? 100 : upperBound);
        return (Collection<String>)(new Object(this, internalUpperBound, internalLowerBound))
                        .getResult();
    }
}
