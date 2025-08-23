package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedConsignmentEntry extends GenericItem
{
    public static final String QUANTITY = "quantity";
    public static final String SHIPPEDQUANTITY = "shippedQuantity";
    public static final String ORDERENTRY = "orderEntry";
    public static final String CONSIGNMENT = "consignment";
    protected static final BidirectionalOneToManyHandler<GeneratedConsignmentEntry> ORDERENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY, false, "orderEntry", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedConsignmentEntry> CONSIGNMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY, false, "consignment", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("shippedQuantity", Item.AttributeMode.INITIAL);
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        tmp.put("consignment", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Consignment getConsignment(SessionContext ctx)
    {
        return (Consignment)getProperty(ctx, "consignment");
    }


    public Consignment getConsignment()
    {
        return getConsignment(getSession().getSessionContext());
    }


    protected void setConsignment(SessionContext ctx, Consignment value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'consignment' is not changeable", 0);
        }
        CONSIGNMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setConsignment(Consignment value)
    {
        setConsignment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERENTRYHANDLER.newInstance(ctx, allAttributes);
        CONSIGNMENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AbstractOrderEntry getOrderEntry(SessionContext ctx)
    {
        return (AbstractOrderEntry)getProperty(ctx, "orderEntry");
    }


    public AbstractOrderEntry getOrderEntry()
    {
        return getOrderEntry(getSession().getSessionContext());
    }


    protected void setOrderEntry(SessionContext ctx, AbstractOrderEntry value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'orderEntry' is not changeable", 0);
        }
        ORDERENTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setOrderEntry(AbstractOrderEntry value)
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


    public Long getShippedQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "shippedQuantity");
    }


    public Long getShippedQuantity()
    {
        return getShippedQuantity(getSession().getSessionContext());
    }


    public long getShippedQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getShippedQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getShippedQuantityAsPrimitive()
    {
        return getShippedQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setShippedQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "shippedQuantity", value);
    }


    public void setShippedQuantity(Long value)
    {
        setShippedQuantity(getSession().getSessionContext(), value);
    }


    public void setShippedQuantity(SessionContext ctx, long value)
    {
        setShippedQuantity(ctx, Long.valueOf(value));
    }


    public void setShippedQuantity(long value)
    {
        setShippedQuantity(getSession().getSessionContext(), value);
    }
}
