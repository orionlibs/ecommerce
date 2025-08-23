package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.ordermodify.jalo.OrderModificationRecordEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderReturnRecordEntry extends OrderModificationRecordEntry
{
    public static final String RETURNSTATUS = "returnStatus";
    public static final String EXPECTEDQUANTITY = "expectedQuantity";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OrderModificationRecordEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("returnStatus", Item.AttributeMode.INITIAL);
        tmp.put("expectedQuantity", Item.AttributeMode.INITIAL);
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


    public EnumerationValue getReturnStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "returnStatus");
    }


    public EnumerationValue getReturnStatus()
    {
        return getReturnStatus(getSession().getSessionContext());
    }


    public void setReturnStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "returnStatus", value);
    }


    public void setReturnStatus(EnumerationValue value)
    {
        setReturnStatus(getSession().getSessionContext(), value);
    }
}
