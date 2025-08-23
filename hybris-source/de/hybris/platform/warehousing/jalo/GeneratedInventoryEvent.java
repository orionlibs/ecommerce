package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedInventoryEvent extends GenericItem
{
    public static final String QUANTITY = "quantity";
    public static final String EVENTDATE = "eventDate";
    public static final String STOCKLEVEL = "stockLevel";
    public static final String CONSIGNMENTENTRY = "consignmentEntry";
    public static final String ORDERENTRY = "orderEntry";
    protected static final BidirectionalOneToManyHandler<GeneratedInventoryEvent> STOCKLEVELHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "stockLevel", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedInventoryEvent> CONSIGNMENTENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "consignmentEntry", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedInventoryEvent> ORDERENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.INVENTORYEVENT, false, "orderEntry", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("eventDate", Item.AttributeMode.INITIAL);
        tmp.put("stockLevel", Item.AttributeMode.INITIAL);
        tmp.put("consignmentEntry", Item.AttributeMode.INITIAL);
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ConsignmentEntry getConsignmentEntry(SessionContext ctx)
    {
        return (ConsignmentEntry)getProperty(ctx, "consignmentEntry");
    }


    public ConsignmentEntry getConsignmentEntry()
    {
        return getConsignmentEntry(getSession().getSessionContext());
    }


    public void setConsignmentEntry(SessionContext ctx, ConsignmentEntry value)
    {
        CONSIGNMENTENTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setConsignmentEntry(ConsignmentEntry value)
    {
        setConsignmentEntry(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        STOCKLEVELHANDLER.newInstance(ctx, allAttributes);
        CONSIGNMENTENTRYHANDLER.newInstance(ctx, allAttributes);
        ORDERENTRYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Date getEventDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "eventDate");
    }


    public Date getEventDate()
    {
        return getEventDate(getSession().getSessionContext());
    }


    public void setEventDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "eventDate", value);
    }


    public void setEventDate(Date value)
    {
        setEventDate(getSession().getSessionContext(), value);
    }


    public OrderEntry getOrderEntry(SessionContext ctx)
    {
        return (OrderEntry)getProperty(ctx, "orderEntry");
    }


    public OrderEntry getOrderEntry()
    {
        return getOrderEntry(getSession().getSessionContext());
    }


    public void setOrderEntry(SessionContext ctx, OrderEntry value)
    {
        ORDERENTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrderEntry(OrderEntry value)
    {
        setOrderEntry(getSession().getSessionContext(), value);
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


    public StockLevel getStockLevel(SessionContext ctx)
    {
        return (StockLevel)getProperty(ctx, "stockLevel");
    }


    public StockLevel getStockLevel()
    {
        return getStockLevel(getSession().getSessionContext());
    }


    public void setStockLevel(SessionContext ctx, StockLevel value)
    {
        STOCKLEVELHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setStockLevel(StockLevel value)
    {
        setStockLevel(getSession().getSessionContext(), value);
    }
}
