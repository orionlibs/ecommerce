package de.hybris.platform.hac.data.dto.cache.region;

import java.util.List;

public class RegionData
{
    private String name;
    private long maxEntries;
    private long maxReachedSize;
    private int factor;
    private CacheStatisticsData cacheStatistics;
    private List<TypeStatisticsData> typesStatistics;


    public List<TypeStatisticsData> getTypesStatistics()
    {
        return this.typesStatistics;
    }


    public void setTypesStatistics(List<TypeStatisticsData> typesStatistics)
    {
        this.typesStatistics = typesStatistics;
    }


    public int getFactor()
    {
        return this.factor;
    }


    public long getMaxEntries()
    {
        return this.maxEntries;
    }


    public long getMaxReachedSize()
    {
        return this.maxReachedSize;
    }


    public String getName()
    {
        return this.name;
    }


    public CacheStatisticsData getCacheStatistics()
    {
        return this.cacheStatistics;
    }


    public void setFactor(int factor)
    {
        this.factor = factor;
    }


    public void setMaxEntries(long maxEntries)
    {
        this.maxEntries = maxEntries;
    }


    public void setMaxReachedSize(long maxReachedSize)
    {
        this.maxReachedSize = maxReachedSize;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setCacheStatistics(CacheStatisticsData cacheStatistics)
    {
        this.cacheStatistics = cacheStatistics;
    }
}
