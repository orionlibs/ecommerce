package de.hybris.platform.jalo.product;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Unit extends GeneratedUnit
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("code", allAttributes, missing) || !checkMandatoryAttribute("unitType", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing parameter " + missing + " got " + allAttributes, 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("unitType", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    public double getConversionFactor()
    {
        return getConversionFactor(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getConversionFactor(SessionContext ctx)
    {
        return getConversionAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setConversionFactor(double factor)
    {
        setConversion(factor);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setConversionFactor(SessionContext ctx, double factor)
    {
        setConversion(ctx, factor);
    }


    public long convert(Unit targetUnit, long value) throws JaloInvalidParameterException
    {
        if(!getUnitType().equals(targetUnit.getUnitType()))
        {
            throw new JaloInvalidParameterException("cant convert: unit types dont match", 290774);
        }
        return (long)CoreAlgorithms.round(CoreAlgorithms.convert(getConversionFactor(), targetUnit.getConversionFactor(), value), 0);
    }


    public double convertExact(Unit targetUnit, double value) throws JaloInvalidParameterException
    {
        if(!getUnitType().equals(targetUnit.getUnitType()))
        {
            throw new JaloInvalidParameterException("cant convert: unit types dont match", 290774);
        }
        return CoreAlgorithms.convert(getConversionFactor(), targetUnit.getConversionFactor(), value);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "Unit '" + getCode() + "' type '" + getUnitType() + "' (" + getPK().getLongValueAsString() + ")";
    }


    public boolean isConvertibleTo(Unit unit)
    {
        if(unit == null)
        {
            return false;
        }
        String unitType1 = getUnitType();
        String unitType2 = unit.getUnitType();
        return (unitType1 == unitType2 || (unitType1 != null && unitType1.equals(unitType2)));
    }


    public int compareQuantity(double value, Unit toUnit, double otherValue)
    {
        if(!isConvertibleTo(toUnit))
        {
            throw new JaloInvalidParameterException("unit " + this + " cannot be compared with unit " + toUnit + " ( values = " + value + " , " + otherValue + " )", 0);
        }
        double result = equals(toUnit) ? otherValue : convertExact(this, otherValue);
        return (value == result) ? 0 : ((value > result) ? 1 : -1);
    }


    public Set<Unit> getConvertibleUnits()
    {
        String myType = getUnitType();
        if(myType == null || myType.length() == 0)
        {
            return Collections.EMPTY_SET;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("t", myType);
        return new HashSet<>(
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getRootComposedTypeForJaloClass(Unit.class)
                                                                        .getCode() + "} WHERE {unitType}=?t AND {" + PK + "}<>?me", params, Unit.class)
                                        .getResult());
    }
}
