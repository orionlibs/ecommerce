package de.hybris.platform.ordermodify.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderModificationRecord extends GenericItem
{
    public static final String INPROGRESS = "inProgress";
    public static final String IDENTIFIER = "identifier";
    public static final String ORDER = "order";
    public static final String MODIFICATIONRECORDENTRIES = "modificationRecordEntries";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderModificationRecord> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERMODIFICATIONRECORD, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<OrderModificationRecordEntry> MODIFICATIONRECORDENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERMODIFICATIONRECORDENTRY, false, "modificationRecord", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("inProgress", Item.AttributeMode.INITIAL);
        tmp.put("identifier", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getIdentifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "identifier");
    }


    public String getIdentifier()
    {
        return getIdentifier(getSession().getSessionContext());
    }


    public void setIdentifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "identifier", value);
    }


    public void setIdentifier(String value)
    {
        setIdentifier(getSession().getSessionContext(), value);
    }


    public Boolean isInProgress(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "inProgress");
    }


    public Boolean isInProgress()
    {
        return isInProgress(getSession().getSessionContext());
    }


    public boolean isInProgressAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInProgress(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInProgressAsPrimitive()
    {
        return isInProgressAsPrimitive(getSession().getSessionContext());
    }


    public void setInProgress(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "inProgress", value);
    }


    public void setInProgress(Boolean value)
    {
        setInProgress(getSession().getSessionContext(), value);
    }


    public void setInProgress(SessionContext ctx, boolean value)
    {
        setInProgress(ctx, Boolean.valueOf(value));
    }


    public void setInProgress(boolean value)
    {
        setInProgress(getSession().getSessionContext(), value);
    }


    public Collection<OrderModificationRecordEntry> getModificationRecordEntries(SessionContext ctx)
    {
        return MODIFICATIONRECORDENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<OrderModificationRecordEntry> getModificationRecordEntries()
    {
        return getModificationRecordEntries(getSession().getSessionContext());
    }


    public void setModificationRecordEntries(SessionContext ctx, Collection<OrderModificationRecordEntry> value)
    {
        MODIFICATIONRECORDENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setModificationRecordEntries(Collection<OrderModificationRecordEntry> value)
    {
        setModificationRecordEntries(getSession().getSessionContext(), value);
    }


    public void addToModificationRecordEntries(SessionContext ctx, OrderModificationRecordEntry value)
    {
        MODIFICATIONRECORDENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToModificationRecordEntries(OrderModificationRecordEntry value)
    {
        addToModificationRecordEntries(getSession().getSessionContext(), value);
    }


    public void removeFromModificationRecordEntries(SessionContext ctx, OrderModificationRecordEntry value)
    {
        MODIFICATIONRECORDENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromModificationRecordEntries(OrderModificationRecordEntry value)
    {
        removeFromModificationRecordEntries(getSession().getSessionContext(), value);
    }


    public Order getOrder(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "order");
    }


    public Order getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, Order value)
    {
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrder(Order value)
    {
        setOrder(getSession().getSessionContext(), value);
    }
}
