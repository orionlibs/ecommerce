package de.hybris.platform.jalo.product;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedUnit extends GenericItem
{
    public static final String CODE = "code";
    public static final String CONVERSION = "conversion";
    public static final String NAME = "name";
    public static final String UNITTYPE = "unitType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("conversion", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("unitType", Item.AttributeMode.INITIAL);
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


    public Double getConversion(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "conversion");
    }


    public Double getConversion()
    {
        return getConversion(getSession().getSessionContext());
    }


    public double getConversionAsPrimitive(SessionContext ctx)
    {
        Double value = getConversion(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getConversionAsPrimitive()
    {
        return getConversionAsPrimitive(getSession().getSessionContext());
    }


    public void setConversion(SessionContext ctx, Double value)
    {
        setProperty(ctx, "conversion", value);
    }


    public void setConversion(Double value)
    {
        setConversion(getSession().getSessionContext(), value);
    }


    public void setConversion(SessionContext ctx, double value)
    {
        setConversion(ctx, Double.valueOf(value));
    }


    public void setConversion(double value)
    {
        setConversion(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedUnit.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedUnit.setName requires a session language", 0);
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
}
