package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedConsignment extends GenericItem
{
    public static final String CODE = "code";
    public static final String SHIPPINGADDRESS = "shippingAddress";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String NAMEDDELIVERYDATE = "namedDeliveryDate";
    public static final String SHIPPINGDATE = "shippingDate";
    public static final String TRACKINGID = "trackingID";
    public static final String CARRIER = "carrier";
    public static final String STATUS = "status";
    public static final String WAREHOUSE = "warehouse";
    public static final String CONSIGNMENTENTRIES = "consignmentEntries";
    public static final String ORDER = "order";
    public static final String CONSIGNMENTPROCESSES = "consignmentProcesses";
    protected static final BidirectionalOneToManyHandler<GeneratedConsignment> WAREHOUSEHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENT, false, "warehouse", null, false, true, 1);
    protected static final OneToManyHandler<ConsignmentEntry> CONSIGNMENTENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY, false, "consignment", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedConsignment> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENT, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<ConsignmentProcess> CONSIGNMENTPROCESSESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTPROCESS, false, "consignment", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("shippingAddress", Item.AttributeMode.INITIAL);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        tmp.put("namedDeliveryDate", Item.AttributeMode.INITIAL);
        tmp.put("shippingDate", Item.AttributeMode.INITIAL);
        tmp.put("trackingID", Item.AttributeMode.INITIAL);
        tmp.put("carrier", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("warehouse", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCarrier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "carrier");
    }


    public String getCarrier()
    {
        return getCarrier(getSession().getSessionContext());
    }


    public void setCarrier(SessionContext ctx, String value)
    {
        setProperty(ctx, "carrier", value);
    }


    public void setCarrier(String value)
    {
        setCarrier(getSession().getSessionContext(), value);
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


    public Set<ConsignmentEntry> getConsignmentEntries(SessionContext ctx)
    {
        return (Set<ConsignmentEntry>)CONSIGNMENTENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<ConsignmentEntry> getConsignmentEntries()
    {
        return getConsignmentEntries(getSession().getSessionContext());
    }


    public void setConsignmentEntries(SessionContext ctx, Set<ConsignmentEntry> value)
    {
        CONSIGNMENTENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConsignmentEntries(Set<ConsignmentEntry> value)
    {
        setConsignmentEntries(getSession().getSessionContext(), value);
    }


    public void addToConsignmentEntries(SessionContext ctx, ConsignmentEntry value)
    {
        CONSIGNMENTENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConsignmentEntries(ConsignmentEntry value)
    {
        addToConsignmentEntries(getSession().getSessionContext(), value);
    }


    public void removeFromConsignmentEntries(SessionContext ctx, ConsignmentEntry value)
    {
        CONSIGNMENTENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConsignmentEntries(ConsignmentEntry value)
    {
        removeFromConsignmentEntries(getSession().getSessionContext(), value);
    }


    public Collection<ConsignmentProcess> getConsignmentProcesses(SessionContext ctx)
    {
        return CONSIGNMENTPROCESSESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<ConsignmentProcess> getConsignmentProcesses()
    {
        return getConsignmentProcesses(getSession().getSessionContext());
    }


    public void setConsignmentProcesses(SessionContext ctx, Collection<ConsignmentProcess> value)
    {
        CONSIGNMENTPROCESSESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConsignmentProcesses(Collection<ConsignmentProcess> value)
    {
        setConsignmentProcesses(getSession().getSessionContext(), value);
    }


    public void addToConsignmentProcesses(SessionContext ctx, ConsignmentProcess value)
    {
        CONSIGNMENTPROCESSESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConsignmentProcesses(ConsignmentProcess value)
    {
        addToConsignmentProcesses(getSession().getSessionContext(), value);
    }


    public void removeFromConsignmentProcesses(SessionContext ctx, ConsignmentProcess value)
    {
        CONSIGNMENTPROCESSESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConsignmentProcesses(ConsignmentProcess value)
    {
        removeFromConsignmentProcesses(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEHANDLER.newInstance(ctx, allAttributes);
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public DeliveryMode getDeliveryMode(SessionContext ctx)
    {
        return (DeliveryMode)getProperty(ctx, "deliveryMode");
    }


    public DeliveryMode getDeliveryMode()
    {
        return getDeliveryMode(getSession().getSessionContext());
    }


    protected void setDeliveryMode(SessionContext ctx, DeliveryMode value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'deliveryMode' is not changeable", 0);
        }
        setProperty(ctx, "deliveryMode", value);
    }


    protected void setDeliveryMode(DeliveryMode value)
    {
        setDeliveryMode(getSession().getSessionContext(), value);
    }


    public Date getNamedDeliveryDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "namedDeliveryDate");
    }


    public Date getNamedDeliveryDate()
    {
        return getNamedDeliveryDate(getSession().getSessionContext());
    }


    protected void setNamedDeliveryDate(SessionContext ctx, Date value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'namedDeliveryDate' is not changeable", 0);
        }
        setProperty(ctx, "namedDeliveryDate", value);
    }


    protected void setNamedDeliveryDate(Date value)
    {
        setNamedDeliveryDate(getSession().getSessionContext(), value);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)getProperty(ctx, "order");
    }


    public AbstractOrder getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    protected void setOrder(SessionContext ctx, AbstractOrder value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'order' is not changeable", 0);
        }
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setOrder(AbstractOrder value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public Address getShippingAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "shippingAddress");
    }


    public Address getShippingAddress()
    {
        return getShippingAddress(getSession().getSessionContext());
    }


    protected void setShippingAddress(SessionContext ctx, Address value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'shippingAddress' is not changeable", 0);
        }
        setProperty(ctx, "shippingAddress", value);
    }


    protected void setShippingAddress(Address value)
    {
        setShippingAddress(getSession().getSessionContext(), value);
    }


    public Date getShippingDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "shippingDate");
    }


    public Date getShippingDate()
    {
        return getShippingDate(getSession().getSessionContext());
    }


    public void setShippingDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "shippingDate", value);
    }


    public void setShippingDate(Date value)
    {
        setShippingDate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public String getTrackingID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "trackingID");
    }


    public String getTrackingID()
    {
        return getTrackingID(getSession().getSessionContext());
    }


    public void setTrackingID(SessionContext ctx, String value)
    {
        setProperty(ctx, "trackingID", value);
    }


    public void setTrackingID(String value)
    {
        setTrackingID(getSession().getSessionContext(), value);
    }


    public Warehouse getWarehouse(SessionContext ctx)
    {
        return (Warehouse)getProperty(ctx, "warehouse");
    }


    public Warehouse getWarehouse()
    {
        return getWarehouse(getSession().getSessionContext());
    }


    protected void setWarehouse(SessionContext ctx, Warehouse value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'warehouse' is not changeable", 0);
        }
        WAREHOUSEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setWarehouse(Warehouse value)
    {
        setWarehouse(getSession().getSessionContext(), value);
    }
}
