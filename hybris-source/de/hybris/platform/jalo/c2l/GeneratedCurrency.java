package de.hybris.platform.jalo.c2l;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCurrency extends C2LItem
{
    public static final String BASE = "base";
    public static final String CONVERSION = "conversion";
    public static final String DIGITS = "digits";
    public static final String SYMBOL = "symbol";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(C2LItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("base", Item.AttributeMode.INITIAL);
        tmp.put("conversion", Item.AttributeMode.INITIAL);
        tmp.put("digits", Item.AttributeMode.INITIAL);
        tmp.put("symbol", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isBase(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "base");
    }


    public Boolean isBase()
    {
        return isBase(getSession().getSessionContext());
    }


    public boolean isBaseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isBase(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isBaseAsPrimitive()
    {
        return isBaseAsPrimitive(getSession().getSessionContext());
    }


    public void setBase(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "base", value);
    }


    public void setBase(Boolean value)
    {
        setBase(getSession().getSessionContext(), value);
    }


    public void setBase(SessionContext ctx, boolean value)
    {
        setBase(ctx, Boolean.valueOf(value));
    }


    public void setBase(boolean value)
    {
        setBase(getSession().getSessionContext(), value);
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


    public Integer getDigits(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "digits");
    }


    public Integer getDigits()
    {
        return getDigits(getSession().getSessionContext());
    }


    public int getDigitsAsPrimitive(SessionContext ctx)
    {
        Integer value = getDigits(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDigitsAsPrimitive()
    {
        return getDigitsAsPrimitive(getSession().getSessionContext());
    }


    public void setDigits(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "digits", value);
    }


    public void setDigits(Integer value)
    {
        setDigits(getSession().getSessionContext(), value);
    }


    public void setDigits(SessionContext ctx, int value)
    {
        setDigits(ctx, Integer.valueOf(value));
    }


    public void setDigits(int value)
    {
        setDigits(getSession().getSessionContext(), value);
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
}
