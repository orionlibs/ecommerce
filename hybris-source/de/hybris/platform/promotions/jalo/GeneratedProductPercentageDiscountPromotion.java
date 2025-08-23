package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductPercentageDiscountPromotion extends ProductPromotion
{
    public static final String PERCENTAGEDISCOUNT = "percentageDiscount";
    public static final String MESSAGEFIRED = "messageFired";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ProductPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("percentageDiscount", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getMessageFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductPercentageDiscountPromotion.getMessageFired requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageFired");
    }


    public String getMessageFired()
    {
        return getMessageFired(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageFired(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageFired", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageFired()
    {
        return getAllMessageFired(getSession().getSessionContext());
    }


    public void setMessageFired(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductPercentageDiscountPromotion.setMessageFired requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageFired", value);
    }


    public void setMessageFired(String value)
    {
        setMessageFired(getSession().getSessionContext(), value);
    }


    public void setAllMessageFired(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageFired", value);
    }


    public void setAllMessageFired(Map<Language, String> value)
    {
        setAllMessageFired(getSession().getSessionContext(), value);
    }


    public Double getPercentageDiscount(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "percentageDiscount");
    }


    public Double getPercentageDiscount()
    {
        return getPercentageDiscount(getSession().getSessionContext());
    }


    public double getPercentageDiscountAsPrimitive(SessionContext ctx)
    {
        Double value = getPercentageDiscount(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getPercentageDiscountAsPrimitive()
    {
        return getPercentageDiscountAsPrimitive(getSession().getSessionContext());
    }


    public void setPercentageDiscount(SessionContext ctx, Double value)
    {
        setProperty(ctx, "percentageDiscount", value);
    }


    public void setPercentageDiscount(Double value)
    {
        setPercentageDiscount(getSession().getSessionContext(), value);
    }


    public void setPercentageDiscount(SessionContext ctx, double value)
    {
        setPercentageDiscount(ctx, Double.valueOf(value));
    }


    public void setPercentageDiscount(double value)
    {
        setPercentageDiscount(getSession().getSessionContext(), value);
    }
}
