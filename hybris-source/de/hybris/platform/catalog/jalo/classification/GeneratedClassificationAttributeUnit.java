package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedClassificationAttributeUnit extends GenericItem
{
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String EXTERNALID = "externalID";
    public static final String SYMBOL = "symbol";
    public static final String UNITTYPE = "unitType";
    public static final String CONVERSIONFACTOR = "conversionFactor";
    public static final String CONVERTIBLEUNITS = "convertibleUnits";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("systemVersion", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("externalID", Item.AttributeMode.INITIAL);
        tmp.put("symbol", Item.AttributeMode.INITIAL);
        tmp.put("unitType", Item.AttributeMode.INITIAL);
        tmp.put("conversionFactor", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Double getConversionFactor(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "conversionFactor");
    }


    public Double getConversionFactor()
    {
        return getConversionFactor(getSession().getSessionContext());
    }


    public double getConversionFactorAsPrimitive(SessionContext ctx)
    {
        Double value = getConversionFactor(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getConversionFactorAsPrimitive()
    {
        return getConversionFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setConversionFactor(SessionContext ctx, Double value)
    {
        setProperty(ctx, "conversionFactor", value);
    }


    public void setConversionFactor(Double value)
    {
        setConversionFactor(getSession().getSessionContext(), value);
    }


    public void setConversionFactor(SessionContext ctx, double value)
    {
        setConversionFactor(ctx, Double.valueOf(value));
    }


    public void setConversionFactor(double value)
    {
        setConversionFactor(getSession().getSessionContext(), value);
    }


    public Set<ClassificationAttributeUnit> getConvertibleUnits()
    {
        return getConvertibleUnits(getSession().getSessionContext());
    }


    public String getExternalID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalID");
    }


    public String getExternalID()
    {
        return getExternalID(getSession().getSessionContext());
    }


    public void setExternalID(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalID", value);
    }


    public void setExternalID(String value)
    {
        setExternalID(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassificationAttributeUnit.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassificationAttributeUnit.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public String getSymbol(SessionContext ctx)
    {
        return (String)getProperty(ctx, "symbol");
    }


    public String getSymbol()
    {
        return getSymbol(getSession().getSessionContext());
    }


    public void setSymbol(SessionContext ctx, String value)
    {
        setProperty(ctx, "symbol", value);
    }


    public void setSymbol(String value)
    {
        setSymbol(getSession().getSessionContext(), value);
    }


    public ClassificationSystemVersion getSystemVersion(SessionContext ctx)
    {
        return (ClassificationSystemVersion)getProperty(ctx, "systemVersion");
    }


    public ClassificationSystemVersion getSystemVersion()
    {
        return getSystemVersion(getSession().getSessionContext());
    }


    protected void setSystemVersion(SessionContext ctx, ClassificationSystemVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'systemVersion' is not changeable", 0);
        }
        setProperty(ctx, "systemVersion", value);
    }


    protected void setSystemVersion(ClassificationSystemVersion value)
    {
        setSystemVersion(getSession().getSessionContext(), value);
    }


    public String getUnitType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "unitType");
    }


    public String getUnitType()
    {
        return getUnitType(getSession().getSessionContext());
    }


    public void setUnitType(SessionContext ctx, String value)
    {
        setProperty(ctx, "unitType", value);
    }


    public void setUnitType(String value)
    {
        setUnitType(getSession().getSessionContext(), value);
    }


    public abstract Set<ClassificationAttributeUnit> getConvertibleUnits(SessionContext paramSessionContext);
}
