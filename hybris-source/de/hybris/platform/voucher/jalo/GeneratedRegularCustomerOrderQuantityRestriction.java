package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRegularCustomerOrderQuantityRestriction extends Restriction
{
    public static final String ORDERQUANTITY = "orderQuantity";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Restriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("orderQuantity", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getOrderQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderQuantity");
    }


    public Integer getOrderQuantity()
    {
        return getOrderQuantity(getSession().getSessionContext());
    }


    public int getOrderQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderQuantityAsPrimitive()
    {
        return getOrderQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setOrderQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderQuantity", value);
    }


    public void setOrderQuantity(Integer value)
    {
        setOrderQuantity(getSession().getSessionContext(), value);
    }


    public void setOrderQuantity(SessionContext ctx, int value)
    {
        setOrderQuantity(ctx, Integer.valueOf(value));
    }


    public void setOrderQuantity(int value)
    {
        setOrderQuantity(getSession().getSessionContext(), value);
    }
}
