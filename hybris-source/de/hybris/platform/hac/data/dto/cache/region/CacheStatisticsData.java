package de.hybris.platform.hac.data.dto.cache.region;

public class CacheStatisticsData
{
    private long hits;
    private long fetches;
    private long misses;
    private long evictions;
    private long invalidations;
    private long instanceCount;


    public long getEvictions()
    {
        return this.evictions;
    }


    public void setHits(long hits)
    {
        this.hits = hits;
    }


    public void setMisses(long misses)
    {
        this.misses = misses;
    }


    public void setEvictions(long evictions)
    {
        this.evictions = evictions;
    }


    public void setInvalidations(long invalidations)
    {
        this.invalidations = invalidations;
    }


    public long getHits()
    {
        return this.hits;
    }


    public long getFetches()
    {
        return this.fetches;
    }


    public void setFetches(long fetches)
    {
        this.fetches = fetches;
    }


    public long getInvalidations()
    {
        return this.invalidations;
    }


    public long getMisses()
    {
        return this.misses;
    }


    public long getInstanceCount()
    {
        return this.instanceCount;
    }


    public void setInstanceCount(long instanceCount)
    {
        this.instanceCount = instanceCount;
    }
}
