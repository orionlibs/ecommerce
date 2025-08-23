package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionPriceRow extends GenericItem
{
    public static final String CURRENCY = "currency";
    public static final String PRICE = "price";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("price", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public Double getPrice(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "price");
    }


    public Double getPrice()
    {
        return getPrice(getSession().getSessionContext());
    }


    public double getPriceAsPrimitive(SessionContext ctx)
    {
        Double value = getPrice(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getPriceAsPrimitive()
    {
        return getPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setPrice(SessionContext ctx, Double value)
    {
        setProperty(ctx, "price", value);
    }


    public void setPrice(Double value)
    {
        setPrice(getSession().getSessionContext(), value);
    }


    public void setPrice(SessionContext ctx, double value)
    {
        setPrice(ctx, Double.valueOf(value));
    }


    public void setPrice(double value)
    {
        setPrice(getSession().getSessionContext(), value);
    }
}
