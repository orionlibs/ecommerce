package de.hybris.platform.ordercancel.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.ordermodify.jalo.OrderEntryModificationRecordEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderEntryCancelRecordEntry extends OrderEntryModificationRecordEntry
{
    public static final String CANCELREQUESTQUANTITY = "cancelRequestQuantity";
    public static final String CANCELLEDQUANTITY = "cancelledQuantity";
    public static final String CANCELREASON = "cancelReason";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OrderEntryModificationRecordEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("cancelRequestQuantity", Item.AttributeMode.INITIAL);
        tmp.put("cancelledQuantity", Item.AttributeMode.INITIAL);
        tmp.put("cancelReason", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getCancelledQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "cancelledQuantity");
    }


    public Integer getCancelledQuantity()
    {
        return getCancelledQuantity(getSession().getSessionContext());
    }


    public int getCancelledQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getCancelledQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCancelledQuantityAsPrimitive()
    {
        return getCancelledQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setCancelledQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "cancelledQuantity", value);
    }


    public void setCancelledQuantity(Integer value)
    {
        setCancelledQuantity(getSession().getSessionContext(), value);
    }


    public void setCancelledQuantity(SessionContext ctx, int value)
    {
        setCancelledQuantity(ctx, Integer.valueOf(value));
    }


    public void setCancelledQuantity(int value)
    {
        setCancelledQuantity(getSession().getSessionContext(), value);
    }


    public EnumerationValue getCancelReason(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "cancelReason");
    }


    public EnumerationValue getCancelReason()
    {
        return getCancelReason(getSession().getSessionContext());
    }


    public void setCancelReason(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "cancelReason", value);
    }


    public void setCancelReason(EnumerationValue value)
    {
        setCancelReason(getSession().getSessionContext(), value);
    }


    public Integer getCancelRequestQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "cancelRequestQuantity");
    }


    public Integer getCancelRequestQuantity()
    {
        return getCancelRequestQuantity(getSession().getSessionContext());
    }


    public int getCancelRequestQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getCancelRequestQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCancelRequestQuantityAsPrimitive()
    {
        return getCancelRequestQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setCancelRequestQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "cancelRequestQuantity", value);
    }


    public void setCancelRequestQuantity(Integer value)
    {
        setCancelRequestQuantity(getSession().getSessionContext(), value);
    }


    public void setCancelRequestQuantity(SessionContext ctx, int value)
    {
        setCancelRequestQuantity(ctx, Integer.valueOf(value));
    }


    public void setCancelRequestQuantity(int value)
    {
        setCancelRequestQuantity(getSession().getSessionContext(), value);
    }
}
