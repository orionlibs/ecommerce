package de.hybris.platform.europe1.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.Discount;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractDiscountRow extends PDTRow
{
    public static final String CURRENCY = "currency";
    public static final String DISCOUNT = "discount";
    public static final String VALUE = "value";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(PDTRow.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("discount", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
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


    public Discount getDiscount(SessionContext ctx)
    {
        return (Discount)getProperty(ctx, "discount");
    }


    public Discount getDiscount()
    {
        return getDiscount(getSession().getSessionContext());
    }


    protected void setDiscount(SessionContext ctx, Discount value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'discount' is not changeable", 0);
        }
        setProperty(ctx, "discount", value);
    }


    protected void setDiscount(Discount value)
    {
        setDiscount(getSession().getSessionContext(), value);
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
}
