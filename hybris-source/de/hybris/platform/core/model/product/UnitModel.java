package de.hybris.platform.core.model.product;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class UnitModel extends ItemModel
{
    public static final String _TYPECODE = "Unit";
    public static final String CODE = "code";
    public static final String CONVERSION = "conversion";
    public static final String NAME = "name";
    public static final String UNITTYPE = "unitType";
    public static final String SAPCODE = "sapCode";


    public UnitModel()
    {
    }


    public UnitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UnitModel(String _code, String _unitType)
    {
        setCode(_code);
        setUnitType(_unitType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UnitModel(String _code, ItemModel _owner, String _unitType)
    {
        setCode(_code);
        setOwner(_owner);
        setUnitType(_unitType);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "conversion", type = Accessor.Type.GETTER)
    public Double getConversion()
    {
        return (Double)getPersistenceContext().getPropertyValue("conversion");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.GETTER)
    public String getSapCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCode");
    }


    @Accessor(qualifier = "unitType", type = Accessor.Type.GETTER)
    public String getUnitType()
    {
        return (String)getPersistenceContext().getPropertyValue("unitType");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "conversion", type = Accessor.Type.SETTER)
    public void setConversion(Double value)
    {
        getPersistenceContext().setPropertyValue("conversion", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.SETTER)
    public void setSapCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapCode", value);
    }


    @Accessor(qualifier = "unitType", type = Accessor.Type.SETTER)
    public void setUnitType(String value)
    {
        getPersistenceContext().setPropertyValue("unitType", value);
    }
}
