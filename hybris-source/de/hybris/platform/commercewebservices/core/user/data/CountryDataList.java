package de.hybris.platform.commercewebservices.core.user.data;

import de.hybris.platform.commercefacades.user.data.CountryData;
import java.io.Serializable;
import java.util.List;

public class CountryDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CountryData> countries;


    public void setCountries(List<CountryData> countries)
    {
        this.countries = countries;
    }


    public List<CountryData> getCountries()
    {
        return this.countries;
    }
}
