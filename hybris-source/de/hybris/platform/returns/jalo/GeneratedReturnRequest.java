package de.hybris.platform.returns.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedReturnRequest extends GenericItem
{
    public static final String CODE = "code";
    public static final String RMA = "RMA";
    public static final String REPLACEMENTORDER = "replacementOrder";
    public static final String CURRENCY = "currency";
    public static final String STATUS = "status";
    public static final String SUBTOTAL = "subtotal";
    public static final String TOTALTAX = "totalTax";
    public static final String RETURNLABEL = "returnLabel";
    public static final String RETURNFORM = "returnForm";
    public static final String RETURNWAREHOUSE = "returnWarehouse";
    public static final String ORDERPOS = "orderPOS";
    public static final String ORDER = "order";
    public static final String RETURNENTRIES = "returnEntries";
    public static final String RETURNPROCESS = "returnProcess";
    protected static final BidirectionalOneToManyHandler<GeneratedReturnRequest> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNREQUEST, false, "order", "orderPOS", true, true, 2);
    protected static final OneToManyHandler<ReturnEntry> RETURNENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNENTRY, true, "returnRequest", "returnRequestPOS", true, true, 2);
    protected static final OneToManyHandler<ReturnProcess> RETURNPROCESSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNPROCESS, false, "returnRequest", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("RMA", Item.AttributeMode.INITIAL);
        tmp.put("replacementOrder", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("subtotal", Item.AttributeMode.INITIAL);
        tmp.put("totalTax", Item.AttributeMode.INITIAL);
        tmp.put("returnLabel", Item.AttributeMode.INITIAL);
        tmp.put("returnForm", Item.AttributeMode.INITIAL);
        tmp.put("returnWarehouse", Item.AttributeMode.INITIAL);
        tmp.put("orderPOS", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
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


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
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


    Integer getOrderPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderPOS");
    }


    Integer getOrderPOS()
    {
        return getOrderPOS(getSession().getSessionContext());
    }


    int getOrderPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getOrderPOSAsPrimitive()
    {
        return getOrderPOSAsPrimitive(getSession().getSessionContext());
    }


    void setOrderPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderPOS", value);
    }


    void setOrderPOS(Integer value)
    {
        setOrderPOS(getSession().getSessionContext(), value);
    }


    void setOrderPOS(SessionContext ctx, int value)
    {
        setOrderPOS(ctx, Integer.valueOf(value));
    }


    void setOrderPOS(int value)
    {
        setOrderPOS(getSession().getSessionContext(), value);
    }


    public ReplacementOrder getReplacementOrder(SessionContext ctx)
    {
        return (ReplacementOrder)getProperty(ctx, "replacementOrder");
    }


    public ReplacementOrder getReplacementOrder()
    {
        return getReplacementOrder(getSession().getSessionContext());
    }


    public void setReplacementOrder(SessionContext ctx, ReplacementOrder value)
    {
        setProperty(ctx, "replacementOrder", value);
    }


    public void setReplacementOrder(ReplacementOrder value)
    {
        setReplacementOrder(getSession().getSessionContext(), value);
    }


    public List<ReturnEntry> getReturnEntries(SessionContext ctx)
    {
        return (List<ReturnEntry>)RETURNENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<ReturnEntry> getReturnEntries()
    {
        return getReturnEntries(getSession().getSessionContext());
    }


    public void setReturnEntries(SessionContext ctx, List<ReturnEntry> value)
    {
        RETURNENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setReturnEntries(List<ReturnEntry> value)
    {
        setReturnEntries(getSession().getSessionContext(), value);
    }


    public void addToReturnEntries(SessionContext ctx, ReturnEntry value)
    {
        RETURNENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToReturnEntries(ReturnEntry value)
    {
        addToReturnEntries(getSession().getSessionContext(), value);
    }


    public void removeFromReturnEntries(SessionContext ctx, ReturnEntry value)
    {
        RETURNENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromReturnEntries(ReturnEntry value)
    {
        removeFromReturnEntries(getSession().getSessionContext(), value);
    }


    public Media getReturnForm(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "returnForm");
    }


    public Media getReturnForm()
    {
        return getReturnForm(getSession().getSessionContext());
    }


    public void setReturnForm(SessionContext ctx, Media value)
    {
        setProperty(ctx, "returnForm", value);
    }


    public void setReturnForm(Media value)
    {
        setReturnForm(getSession().getSessionContext(), value);
    }


    public Media getReturnLabel(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "returnLabel");
    }


    public Media getReturnLabel()
    {
        return getReturnLabel(getSession().getSessionContext());
    }


    public void setReturnLabel(SessionContext ctx, Media value)
    {
        setProperty(ctx, "returnLabel", value);
    }


    public void setReturnLabel(Media value)
    {
        setReturnLabel(getSession().getSessionContext(), value);
    }


    public Collection<ReturnProcess> getReturnProcess(SessionContext ctx)
    {
        return RETURNPROCESSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<ReturnProcess> getReturnProcess()
    {
        return getReturnProcess(getSession().getSessionContext());
    }


    public void setReturnProcess(SessionContext ctx, Collection<ReturnProcess> value)
    {
        RETURNPROCESSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setReturnProcess(Collection<ReturnProcess> value)
    {
        setReturnProcess(getSession().getSessionContext(), value);
    }


    public void addToReturnProcess(SessionContext ctx, ReturnProcess value)
    {
        RETURNPROCESSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToReturnProcess(ReturnProcess value)
    {
        addToReturnProcess(getSession().getSessionContext(), value);
    }


    public void removeFromReturnProcess(SessionContext ctx, ReturnProcess value)
    {
        RETURNPROCESSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromReturnProcess(ReturnProcess value)
    {
        removeFromReturnProcess(getSession().getSessionContext(), value);
    }


    public Warehouse getReturnWarehouse(SessionContext ctx)
    {
        return (Warehouse)getProperty(ctx, "returnWarehouse");
    }


    public Warehouse getReturnWarehouse()
    {
        return getReturnWarehouse(getSession().getSessionContext());
    }


    public void setReturnWarehouse(SessionContext ctx, Warehouse value)
    {
        setProperty(ctx, "returnWarehouse", value);
    }


    public void setReturnWarehouse(Warehouse value)
    {
        setReturnWarehouse(getSession().getSessionContext(), value);
    }


    public String getRMA(SessionContext ctx)
    {
        return (String)getProperty(ctx, "RMA");
    }


    public String getRMA()
    {
        return getRMA(getSession().getSessionContext());
    }


    public void setRMA(SessionContext ctx, String value)
    {
        setProperty(ctx, "RMA", value);
    }


    public void setRMA(String value)
    {
        setRMA(getSession().getSessionContext(), value);
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


    public BigDecimal getSubtotal(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "subtotal");
    }


    public BigDecimal getSubtotal()
    {
        return getSubtotal(getSession().getSessionContext());
    }


    public void setSubtotal(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "subtotal", value);
    }


    public void setSubtotal(BigDecimal value)
    {
        setSubtotal(getSession().getSessionContext(), value);
    }


    public BigDecimal getTotalTax(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "totalTax");
    }


    public BigDecimal getTotalTax()
    {
        return getTotalTax(getSession().getSessionContext());
    }


    public void setTotalTax(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "totalTax", value);
    }


    public void setTotalTax(BigDecimal value)
    {
        setTotalTax(getSession().getSessionContext(), value);
    }
}
