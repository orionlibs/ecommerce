package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RegionModel extends C2LItemModel
{
    public static final String _TYPECODE = "Region";
    public static final String _COUNTRY2REGIONRELATION = "Country2RegionRelation";
    public static final String COUNTRY = "country";
    public static final String ISOCODESHORT = "isocodeShort";


    public RegionModel()
    {
    }


    public RegionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegionModel(CountryModel _country, String _isocode)
    {
        setCountry(_country);
        setIsocode(_isocode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegionModel(CountryModel _country, String _isocode, ItemModel _owner)
    {
        setCountry(_country);
        setIsocode(_isocode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "country", type = Accessor.Type.GETTER)
    public CountryModel getCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("country");
    }


    @Accessor(qualifier = "isocodeShort", type = Accessor.Type.GETTER)
    public String getIsocodeShort()
    {
        return (String)getPersistenceContext().getPropertyValue("isocodeShort");
    }


    @Accessor(qualifier = "country", type = Accessor.Type.SETTER)
    public void setCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("country", value);
    }


    @Accessor(qualifier = "isocodeShort", type = Accessor.Type.SETTER)
    public void setIsocodeShort(String value)
    {
        getPersistenceContext().setPropertyValue("isocodeShort", value);
    }
}
