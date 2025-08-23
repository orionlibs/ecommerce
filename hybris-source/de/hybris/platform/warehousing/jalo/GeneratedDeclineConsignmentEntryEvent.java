package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDeclineConsignmentEntryEvent extends ConsignmentEntryEvent
{
    public static final String REASON = "reason";
    public static final String REALLOCATEDWAREHOUSE = "reallocatedWarehouse";
    public static final String CONSIGNMENTENTRY = "consignmentEntry";
    protected static final BidirectionalOneToManyHandler<GeneratedDeclineConsignmentEntryEvent> CONSIGNMENTENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.DECLINECONSIGNMENTENTRYEVENT, false, "consignmentEntry", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ConsignmentEntryEvent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("reason", Item.AttributeMode.INITIAL);
        tmp.put("reallocatedWarehouse", Item.AttributeMode.INITIAL);
        tmp.put("consignmentEntry", Item.AttributeMode.INITIAL);
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
        CONSIGNMENTENTRYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Warehouse getReallocatedWarehouse(SessionContext ctx)
    {
        return (Warehouse)getProperty(ctx, "reallocatedWarehouse");
    }


    public Warehouse getReallocatedWarehouse()
    {
        return getReallocatedWarehouse(getSession().getSessionContext());
    }


    public void setReallocatedWarehouse(SessionContext ctx, Warehouse value)
    {
        setProperty(ctx, "reallocatedWarehouse", value);
    }


    public void setReallocatedWarehouse(Warehouse value)
    {
        setReallocatedWarehouse(getSession().getSessionContext(), value);
    }


    public EnumerationValue getReason(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "reason");
    }


    public EnumerationValue getReason()
    {
        return getReason(getSession().getSessionContext());
    }


    public void setReason(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "reason", value);
    }


    public void setReason(EnumerationValue value)
    {
        setReason(getSession().getSessionContext(), value);
    }
}
