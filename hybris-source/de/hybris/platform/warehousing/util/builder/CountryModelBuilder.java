package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.CountryModel;
import java.util.Locale;

public class CountryModelBuilder
{
    private final CountryModel model = new CountryModel();


    private CountryModel getModel()
    {
        return this.model;
    }


    public static CountryModelBuilder aModel()
    {
        return new CountryModelBuilder();
    }


    public static CountryModelBuilder fromModel(CountryModel model)
    {
        return new CountryModelBuilder();
    }


    public CountryModel build()
    {
        return getModel();
    }


    public CountryModelBuilder withIsoCode(String isoCode)
    {
        getModel().setIsocode(isoCode);
        return this;
    }


    public CountryModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public CountryModelBuilder withActive(Boolean active)
    {
        getModel().setActive(active);
        return this;
    }
}
