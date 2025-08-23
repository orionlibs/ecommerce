package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Unit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductQuantityRestriction extends ProductRestriction
{
    public static final String QUANTITY = "quantity";
    public static final String UNIT = "unit";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ProductRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("unit", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Long getQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "quantity");
    }


    public Long getQuantity()
    {
        return getQuantity(getSession().getSessionContext());
    }


    public long getQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getQuantityAsPrimitive()
    {
        return getQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "quantity", value);
    }


    public void setQuantity(Long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public void setQuantity(SessionContext ctx, long value)
    {
        setQuantity(ctx, Long.valueOf(value));
    }


    public void setQuantity(long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public Unit getUnit(SessionContext ctx)
    {
        return (Unit)getProperty(ctx, "unit");
    }


    public Unit getUnit()
    {
        return getUnit(getSession().getSessionContext());
    }


    public void setUnit(SessionContext ctx, Unit value)
    {
        setProperty(ctx, "unit", value);
    }


    public void setUnit(Unit value)
    {
        setUnit(getSession().getSessionContext(), value);
    }
}
