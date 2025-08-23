package de.hybris.platform.regioncache.region;

public class CacheRegionRegistrar
{
    private final CacheRegion region;


    public CacheRegionRegistrar(CacheRegion region)
    {
        this.region = region;
    }


    public CacheRegion getRegion()
    {
        return this.region;
    }
}
