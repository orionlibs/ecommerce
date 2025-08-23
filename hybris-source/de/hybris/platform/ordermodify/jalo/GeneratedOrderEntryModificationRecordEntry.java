package de.hybris.platform.ordermodify.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderEntryModificationRecordEntry extends GenericItem
{
    public static final String CODE = "code";
    public static final String NOTES = "notes";
    public static final String ORIGINALORDERENTRY = "originalOrderEntry";
    public static final String ORDERENTRY = "orderEntry";
    public static final String MODIFICATIONRECORDENTRY = "modificationRecordEntry";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderEntryModificationRecordEntry> MODIFICATIONRECORDENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERENTRYMODIFICATIONRECORDENTRY, false, "modificationRecordEntry", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("notes", Item.AttributeMode.INITIAL);
        tmp.put("originalOrderEntry", Item.AttributeMode.INITIAL);
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        tmp.put("modificationRecordEntry", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MODIFICATIONRECORDENTRYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public OrderModificationRecordEntry getModificationRecordEntry(SessionContext ctx)
    {
        return (OrderModificationRecordEntry)getProperty(ctx, "modificationRecordEntry");
    }


    public OrderModificationRecordEntry getModificationRecordEntry()
    {
        return getModificationRecordEntry(getSession().getSessionContext());
    }


    public void setModificationRecordEntry(SessionContext ctx, OrderModificationRecordEntry value)
    {
        MODIFICATIONRECORDENTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setModificationRecordEntry(OrderModificationRecordEntry value)
    {
        setModificationRecordEntry(getSession().getSessionContext(), value);
    }


    public String getNotes(SessionContext ctx)
    {
        return (String)getProperty(ctx, "notes");
    }


    public String getNotes()
    {
        return getNotes(getSession().getSessionContext());
    }


    public void setNotes(SessionContext ctx, String value)
    {
        setProperty(ctx, "notes", value);
    }


    public void setNotes(String value)
    {
        setNotes(getSession().getSessionContext(), value);
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
        setProperty(ctx, "orderEntry", value);
    }


    public void setOrderEntry(OrderEntry value)
    {
        setOrderEntry(getSession().getSessionContext(), value);
    }


    public OrderEntry getOriginalOrderEntry(SessionContext ctx)
    {
        return (OrderEntry)getProperty(ctx, "originalOrderEntry");
    }


    public OrderEntry getOriginalOrderEntry()
    {
        return getOriginalOrderEntry(getSession().getSessionContext());
    }


    public void setOriginalOrderEntry(SessionContext ctx, OrderEntry value)
    {
        setProperty(ctx, "originalOrderEntry", value);
    }


    public void setOriginalOrderEntry(OrderEntry value)
    {
        setOriginalOrderEntry(getSession().getSessionContext(), value);
    }
}
