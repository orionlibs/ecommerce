package de.hybris.platform.commercewebservices.core.store.data;

import de.hybris.platform.commercefacades.store.data.StoreCountData;
import java.io.Serializable;
import java.util.List;

public class StoreCountListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<StoreCountData> countriesAndRegionsStoreCount;


    public void setCountriesAndRegionsStoreCount(List<StoreCountData> countriesAndRegionsStoreCount)
    {
        this.countriesAndRegionsStoreCount = countriesAndRegionsStoreCount;
    }


    public List<StoreCountData> getCountriesAndRegionsStoreCount()
    {
        return this.countriesAndRegionsStoreCount;
    }
}
