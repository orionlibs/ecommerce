package de.hybris.platform.hac.data.dto.cache.region;

import java.util.List;

public class RegionCacheData
{
    private int totalRegionCount;
    private List<RegionData> regions;


    public int getTotalRegionCount()
    {
        return this.totalRegionCount;
    }


    public void setTotalRegionCount(int totalRegionCount)
    {
        this.totalRegionCount = totalRegionCount;
    }


    public List<RegionData> getRegions()
    {
        return this.regions;
    }


    public void setRegions(List<RegionData> regions)
    {
        this.regions = regions;
    }
}
