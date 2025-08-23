package de.hybris.platform.commercewebservices.core.basesite.data;

import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import java.io.Serializable;
import java.util.List;

public class BaseSiteDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<BaseSiteData> baseSites;


    public void setBaseSites(List<BaseSiteData> baseSites)
    {
        this.baseSites = baseSites;
    }


    public List<BaseSiteData> getBaseSites()
    {
        return this.baseSites;
    }
}
