package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ordermodify.jalo.OrderEntryModificationRecordEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderEntryReturnRecordEntry extends OrderEntryModificationRecordEntry
{
    public static final String EXPECTEDQUANTITY = "expectedQuantity";
    public static final String RETURNEDQUANTITY = "returnedQuantity";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OrderEntryModificationRecordEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("expectedQuantity", Item.AttributeMode.INITIAL);
        tmp.put("returnedQuantity", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Long getExpectedQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "expectedQuantity");
    }


    public Long getExpectedQuantity()
    {
        return getExpectedQuantity(getSession().getSessionContext());
    }


    public long getExpectedQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getExpectedQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getExpectedQuantityAsPrimitive()
    {
        return getExpectedQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setExpectedQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "expectedQuantity", value);
    }


    public void setExpectedQuantity(Long value)
    {
        setExpectedQuantity(getSession().getSessionContext(), value);
    }


    public void setExpectedQuantity(SessionContext ctx, long value)
    {
        setExpectedQuantity(ctx, Long.valueOf(value));
    }


    public void setExpectedQuantity(long value)
    {
        setExpectedQuantity(getSession().getSessionContext(), value);
    }


    public Long getReturnedQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "returnedQuantity");
    }


    public Long getReturnedQuantity()
    {
        return getReturnedQuantity(getSession().getSessionContext());
    }


    public long getReturnedQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getReturnedQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getReturnedQuantityAsPrimitive()
    {
        return getReturnedQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setReturnedQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "returnedQuantity", value);
    }


    public void setReturnedQuantity(Long value)
    {
        setReturnedQuantity(getSession().getSessionContext(), value);
    }


    public void setReturnedQuantity(SessionContext ctx, long value)
    {
        setReturnedQuantity(ctx, Long.valueOf(value));
    }


    public void setReturnedQuantity(long value)
    {
        setReturnedQuantity(getSession().getSessionContext(), value);
    }
}
