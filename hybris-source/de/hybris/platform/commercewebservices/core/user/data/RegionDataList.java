package de.hybris.platform.commercewebservices.core.user.data;

import de.hybris.platform.commercefacades.user.data.RegionData;
import java.io.Serializable;
import java.util.List;

public class RegionDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<RegionData> regions;


    public void setRegions(List<RegionData> regions)
    {
        this.regions = regions;
    }


    public List<RegionData> getRegions()
    {
        return this.regions;
    }
}
