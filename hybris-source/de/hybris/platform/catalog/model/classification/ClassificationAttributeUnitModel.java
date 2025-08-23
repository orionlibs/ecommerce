package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

public class ClassificationAttributeUnitModel extends ItemModel
{
    public static final String _TYPECODE = "ClassificationAttributeUnit";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String EXTERNALID = "externalID";
    public static final String SYMBOL = "symbol";
    public static final String UNITTYPE = "unitType";
    public static final String CONVERSIONFACTOR = "conversionFactor";
    public static final String CONVERTIBLEUNITS = "convertibleUnits";
    public static final String SAPCODE = "sapCode";


    public ClassificationAttributeUnitModel()
    {
    }


    public ClassificationAttributeUnitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeUnitModel(String _code, String _symbol, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setSymbol(_symbol);
        setSystemVersion(_systemVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeUnitModel(String _code, ItemModel _owner, String _symbol, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setOwner(_owner);
        setSymbol(_symbol);
        setSystemVersion(_systemVersion);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "conversionFactor", type = Accessor.Type.GETTER)
    public Double getConversionFactor()
    {
        return (Double)getPersistenceContext().getPropertyValue("conversionFactor");
    }


    @Accessor(qualifier = "convertibleUnits", type = Accessor.Type.GETTER)
    public Set<ClassificationAttributeUnitModel> getConvertibleUnits()
    {
        return (Set<ClassificationAttributeUnitModel>)getPersistenceContext().getPropertyValue("convertibleUnits");
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
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


    @Accessor(qualifier = "symbol", type = Accessor.Type.GETTER)
    public String getSymbol()
    {
        return (String)getPersistenceContext().getPropertyValue("symbol");
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getSystemVersion()
    {
        return (ClassificationSystemVersionModel)getPersistenceContext().getPropertyValue("systemVersion");
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


    @Accessor(qualifier = "conversionFactor", type = Accessor.Type.SETTER)
    public void setConversionFactor(Double value)
    {
        getPersistenceContext().setPropertyValue("conversionFactor", value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
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


    @Accessor(qualifier = "symbol", type = Accessor.Type.SETTER)
    public void setSymbol(String value)
    {
        getPersistenceContext().setPropertyValue("symbol", value);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
    public void setSystemVersion(ClassificationSystemVersionModel value)
    {
        getPersistenceContext().setPropertyValue("systemVersion", value);
    }


    @Accessor(qualifier = "unitType", type = Accessor.Type.SETTER)
    public void setUnitType(String value)
    {
        getPersistenceContext().setPropertyValue("unitType", value);
    }
}
