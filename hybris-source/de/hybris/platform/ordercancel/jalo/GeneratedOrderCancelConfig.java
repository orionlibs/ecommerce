package de.hybris.platform.ordercancel.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderCancelConfig extends GenericItem
{
    public static final String ORDERCANCELALLOWED = "orderCancelAllowed";
    public static final String CANCELAFTERWAREHOUSEALLOWED = "cancelAfterWarehouseAllowed";
    public static final String COMPLETECANCELAFTERSHIPPINGSTARTEDALLOWED = "completeCancelAfterShippingStartedAllowed";
    public static final String PARTIALCANCELALLOWED = "partialCancelAllowed";
    public static final String PARTIALORDERENTRYCANCELALLOWED = "partialOrderEntryCancelAllowed";
    public static final String QUEUEDORDERWAITINGTIME = "queuedOrderWaitingTime";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("orderCancelAllowed", Item.AttributeMode.INITIAL);
        tmp.put("cancelAfterWarehouseAllowed", Item.AttributeMode.INITIAL);
        tmp.put("completeCancelAfterShippingStartedAllowed", Item.AttributeMode.INITIAL);
        tmp.put("partialCancelAllowed", Item.AttributeMode.INITIAL);
        tmp.put("partialOrderEntryCancelAllowed", Item.AttributeMode.INITIAL);
        tmp.put("queuedOrderWaitingTime", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCancelAfterWarehouseAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "cancelAfterWarehouseAllowed");
    }


    public Boolean isCancelAfterWarehouseAllowed()
    {
        return isCancelAfterWarehouseAllowed(getSession().getSessionContext());
    }


    public boolean isCancelAfterWarehouseAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCancelAfterWarehouseAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCancelAfterWarehouseAllowedAsPrimitive()
    {
        return isCancelAfterWarehouseAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setCancelAfterWarehouseAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "cancelAfterWarehouseAllowed", value);
    }


    public void setCancelAfterWarehouseAllowed(Boolean value)
    {
        setCancelAfterWarehouseAllowed(getSession().getSessionContext(), value);
    }


    public void setCancelAfterWarehouseAllowed(SessionContext ctx, boolean value)
    {
        setCancelAfterWarehouseAllowed(ctx, Boolean.valueOf(value));
    }


    public void setCancelAfterWarehouseAllowed(boolean value)
    {
        setCancelAfterWarehouseAllowed(getSession().getSessionContext(), value);
    }


    public Boolean isCompleteCancelAfterShippingStartedAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "completeCancelAfterShippingStartedAllowed");
    }


    public Boolean isCompleteCancelAfterShippingStartedAllowed()
    {
        return isCompleteCancelAfterShippingStartedAllowed(getSession().getSessionContext());
    }


    public boolean isCompleteCancelAfterShippingStartedAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCompleteCancelAfterShippingStartedAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCompleteCancelAfterShippingStartedAllowedAsPrimitive()
    {
        return isCompleteCancelAfterShippingStartedAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setCompleteCancelAfterShippingStartedAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "completeCancelAfterShippingStartedAllowed", value);
    }


    public void setCompleteCancelAfterShippingStartedAllowed(Boolean value)
    {
        setCompleteCancelAfterShippingStartedAllowed(getSession().getSessionContext(), value);
    }


    public void setCompleteCancelAfterShippingStartedAllowed(SessionContext ctx, boolean value)
    {
        setCompleteCancelAfterShippingStartedAllowed(ctx, Boolean.valueOf(value));
    }


    public void setCompleteCancelAfterShippingStartedAllowed(boolean value)
    {
        setCompleteCancelAfterShippingStartedAllowed(getSession().getSessionContext(), value);
    }


    public Boolean isOrderCancelAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "orderCancelAllowed");
    }


    public Boolean isOrderCancelAllowed()
    {
        return isOrderCancelAllowed(getSession().getSessionContext());
    }


    public boolean isOrderCancelAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOrderCancelAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOrderCancelAllowedAsPrimitive()
    {
        return isOrderCancelAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setOrderCancelAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "orderCancelAllowed", value);
    }


    public void setOrderCancelAllowed(Boolean value)
    {
        setOrderCancelAllowed(getSession().getSessionContext(), value);
    }


    public void setOrderCancelAllowed(SessionContext ctx, boolean value)
    {
        setOrderCancelAllowed(ctx, Boolean.valueOf(value));
    }


    public void setOrderCancelAllowed(boolean value)
    {
        setOrderCancelAllowed(getSession().getSessionContext(), value);
    }


    public Boolean isPartialCancelAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "partialCancelAllowed");
    }


    public Boolean isPartialCancelAllowed()
    {
        return isPartialCancelAllowed(getSession().getSessionContext());
    }


    public boolean isPartialCancelAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPartialCancelAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPartialCancelAllowedAsPrimitive()
    {
        return isPartialCancelAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setPartialCancelAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "partialCancelAllowed", value);
    }


    public void setPartialCancelAllowed(Boolean value)
    {
        setPartialCancelAllowed(getSession().getSessionContext(), value);
    }


    public void setPartialCancelAllowed(SessionContext ctx, boolean value)
    {
        setPartialCancelAllowed(ctx, Boolean.valueOf(value));
    }


    public void setPartialCancelAllowed(boolean value)
    {
        setPartialCancelAllowed(getSession().getSessionContext(), value);
    }


    public Boolean isPartialOrderEntryCancelAllowed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "partialOrderEntryCancelAllowed");
    }


    public Boolean isPartialOrderEntryCancelAllowed()
    {
        return isPartialOrderEntryCancelAllowed(getSession().getSessionContext());
    }


    public boolean isPartialOrderEntryCancelAllowedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPartialOrderEntryCancelAllowed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPartialOrderEntryCancelAllowedAsPrimitive()
    {
        return isPartialOrderEntryCancelAllowedAsPrimitive(getSession().getSessionContext());
    }


    public void setPartialOrderEntryCancelAllowed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "partialOrderEntryCancelAllowed", value);
    }


    public void setPartialOrderEntryCancelAllowed(Boolean value)
    {
        setPartialOrderEntryCancelAllowed(getSession().getSessionContext(), value);
    }


    public void setPartialOrderEntryCancelAllowed(SessionContext ctx, boolean value)
    {
        setPartialOrderEntryCancelAllowed(ctx, Boolean.valueOf(value));
    }


    public void setPartialOrderEntryCancelAllowed(boolean value)
    {
        setPartialOrderEntryCancelAllowed(getSession().getSessionContext(), value);
    }


    public Integer getQueuedOrderWaitingTime(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "queuedOrderWaitingTime");
    }


    public Integer getQueuedOrderWaitingTime()
    {
        return getQueuedOrderWaitingTime(getSession().getSessionContext());
    }


    public int getQueuedOrderWaitingTimeAsPrimitive(SessionContext ctx)
    {
        Integer value = getQueuedOrderWaitingTime(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQueuedOrderWaitingTimeAsPrimitive()
    {
        return getQueuedOrderWaitingTimeAsPrimitive(getSession().getSessionContext());
    }


    public void setQueuedOrderWaitingTime(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "queuedOrderWaitingTime", value);
    }


    public void setQueuedOrderWaitingTime(Integer value)
    {
        setQueuedOrderWaitingTime(getSession().getSessionContext(), value);
    }


    public void setQueuedOrderWaitingTime(SessionContext ctx, int value)
    {
        setQueuedOrderWaitingTime(ctx, Integer.valueOf(value));
    }


    public void setQueuedOrderWaitingTime(int value)
    {
        setQueuedOrderWaitingTime(getSession().getSessionContext(), value);
    }
}
