package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassificationAttributeUnit extends GeneratedClassificationAttributeUnit
{
    private static final Logger LOG = Logger.getLogger(ClassificationAttributeUnit.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get("code");
        String symbol = (String)allAttributes.get("symbol");
        if(code == null && symbol == null)
        {
            throw new JaloInvalidParameterException("must provide at least one of code or symbol to create a new " + type
                            .getCode(), 0);
        }
        if(allAttributes.get("conversionFactor") != null && ((Double)allAttributes.get("conversionFactor")).doubleValue() <= 0.0D)
        {
            throw new JaloInvalidParameterException("illegal conversionFactor " + allAttributes.get("conversionFactor") + " create a new " + type
                            .getCode() + " : must be > 0", 0);
        }
        if(code == null || symbol == null)
        {
            Item.ItemAttributeMap myMap = new Item.ItemAttributeMap((Map)allAttributes);
            if(code == null)
            {
                myMap.put("code", symbol);
            }
            if(symbol == null)
            {
                myMap.put("symbol", code);
            }
            myMap.setAttributeMode("code", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("symbol", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("unitType", Item.AttributeMode.INITIAL);
            myMap.setAttributeMode("conversionFactor", Item.AttributeMode.INITIAL);
            return super.createItem(ctx, type, myMap);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("symbol", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("unitType", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("conversionFactor", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCode() + "(" + getCode() + ")";
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public Set<ClassificationAttributeUnit> getConvertibleUnits(SessionContext ctx)
    {
        String unitType = getUnitType(ctx);
        if(unitType == null)
        {
            return Collections.EMPTY_SET;
        }
        Map<Object, Object> values = new HashMap<>();
        values.put("type", unitType);
        values.put("me", getPK());
        values.put("version", getSystemVersion());
        return new LinkedHashSet<>(
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} WHERE {unitType}=?type AND {" + Item.PK + "}<> ?me AND {systemVersion}=?version ORDER BY {code} ASC ", values,
                                                        Collections.singletonList(ClassificationAttributeUnit.class), true, true, 0, -1)
                                        .getResult());
    }


    public double convertTo(double doubleValue, ClassificationAttributeUnit otherUnit)
    {
        if(equals(otherUnit))
        {
            return doubleValue;
        }
        return CoreAlgorithms.convert(getConversionFactorAsPrimitive(), otherUnit.getConversionFactorAsPrimitive(), doubleValue);
    }
}
