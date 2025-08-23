package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import java.util.Locale;

public class RegionModelBuilder
{
    private final RegionModel model = new RegionModel();


    public static RegionModelBuilder aModel()
    {
        return new RegionModelBuilder();
    }


    private RegionModel getModel()
    {
        return this.model;
    }


    public RegionModel build()
    {
        return getModel();
    }


    public RegionModelBuilder withCountry(CountryModel country)
    {
        getModel().setCountry(country);
        return this;
    }


    public RegionModelBuilder withIsocodeShort(String isocodeShort)
    {
        getModel().setIsocodeShort(isocodeShort);
        return this;
    }


    public RegionModelBuilder withIsocode(String isocode)
    {
        getModel().setIsocode(isocode);
        return this;
    }


    public RegionModelBuilder withName(String name)
    {
        getModel().setName(name, Locale.ENGLISH);
        return this;
    }
}
