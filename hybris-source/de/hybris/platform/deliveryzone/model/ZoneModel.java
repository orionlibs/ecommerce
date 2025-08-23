package de.hybris.platform.deliveryzone.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class ZoneModel extends ItemModel
{
    public static final String _TYPECODE = "Zone";
    public static final String CODE = "code";
    public static final String COUNTRIES = "countries";


    public ZoneModel()
    {
    }


    public ZoneModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ZoneModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "countries", type = Accessor.Type.GETTER)
    public Set<CountryModel> getCountries()
    {
        return (Set<CountryModel>)getPersistenceContext().getPropertyValue("countries");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "countries", type = Accessor.Type.SETTER)
    public void setCountries(Set<CountryModel> value)
    {
        getPersistenceContext().setPropertyValue("countries", value);
    }
}
