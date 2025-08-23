package de.hybris.platform.hac.data.dto.cache.region;

public class TypeStatisticsData
{
    private Object type;
    private String typeName;
    private long hits;
    private long fetches;
    private long misses;
    private long evictions;
    private long invalidations;


    public Object getType()
    {
        return this.type;
    }


    public void setType(Object type)
    {
        this.type = type;
    }


    public String getTypeName()
    {
        return this.typeName;
    }


    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public long getHits()
    {
        return this.hits;
    }


    public void setHits(long hits)
    {
        this.hits = hits;
    }


    public long getFetches()
    {
        return this.fetches;
    }


    public void setFetches(long fetches)
    {
        this.fetches = fetches;
    }


    public long getMisses()
    {
        return this.misses;
    }


    public void setMisses(long misses)
    {
        this.misses = misses;
    }


    public long getEvictions()
    {
        return this.evictions;
    }


    public void setEvictions(long evictions)
    {
        this.evictions = evictions;
    }


    public long getInvalidations()
    {
        return this.invalidations;
    }


    public void setInvalidations(long invalidations)
    {
        this.invalidations = invalidations;
    }
}
