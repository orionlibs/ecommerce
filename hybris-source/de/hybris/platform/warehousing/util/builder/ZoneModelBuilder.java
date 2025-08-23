package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;

public class ZoneModelBuilder
{
    private final ZoneModel model = new ZoneModel();


    private ZoneModel getModel()
    {
        return this.model;
    }


    public static ZoneModelBuilder aModel()
    {
        return new ZoneModelBuilder();
    }


    public ZoneModel build()
    {
        return getModel();
    }


    public ZoneModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ZoneModelBuilder withCountries(CountryModel... countries)
    {
        getModel().setCountries(Sets.newHashSet((Object[])countries));
        return this;
    }
}
