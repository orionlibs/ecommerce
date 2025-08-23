package de.hybris.platform.jalo.order.price;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTax extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String CURRENCY = "currency";
    public static final String ABSOLUTE = "absolute";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAbsolute()
    {
        return isAbsolute(getSession().getSessionContext());
    }


    public boolean isAbsoluteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAbsolute(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAbsoluteAsPrimitive()
    {
        return isAbsoluteAsPrimitive(getSession().getSessionContext());
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value) throws ConsistencyCheckException
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedTax.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedTax.setName requires a session language", 0);
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


    public Double getValue(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "value");
    }


    public Double getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public double getValueAsPrimitive(SessionContext ctx)
    {
        Double value = getValue(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getValueAsPrimitive()
    {
        return getValueAsPrimitive(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, Double value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(Double value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public void setValue(SessionContext ctx, double value)
    {
        setValue(ctx, Double.valueOf(value));
    }


    public void setValue(double value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public abstract Boolean isAbsolute(SessionContext paramSessionContext);
}
