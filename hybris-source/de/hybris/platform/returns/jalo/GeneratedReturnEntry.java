package de.hybris.platform.returns.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedReturnEntry extends GenericItem
{
    public static final String ORDERENTRY = "orderEntry";
    public static final String EXPECTEDQUANTITY = "expectedQuantity";
    public static final String RECEIVEDQUANTITY = "receivedQuantity";
    public static final String REACHEDDATE = "reachedDate";
    public static final String STATUS = "status";
    public static final String ACTION = "action";
    public static final String NOTES = "notes";
    public static final String TAX = "tax";
    public static final String RETURNREQUESTPOS = "returnRequestPOS";
    public static final String RETURNREQUEST = "returnRequest";
    protected static final BidirectionalOneToManyHandler<GeneratedReturnEntry> RETURNREQUESTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNENTRY, false, "returnRequest", "returnRequestPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        tmp.put("expectedQuantity", Item.AttributeMode.INITIAL);
        tmp.put("receivedQuantity", Item.AttributeMode.INITIAL);
        tmp.put("reachedDate", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("action", Item.AttributeMode.INITIAL);
        tmp.put("notes", Item.AttributeMode.INITIAL);
        tmp.put("tax", Item.AttributeMode.INITIAL);
        tmp.put("returnRequestPOS", Item.AttributeMode.INITIAL);
        tmp.put("returnRequest", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getAction(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "action");
    }


    public EnumerationValue getAction()
    {
        return getAction(getSession().getSessionContext());
    }


    public void setAction(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "action", value);
    }


    public void setAction(EnumerationValue value)
    {
        setAction(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        RETURNREQUESTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public AbstractOrderEntry getOrderEntry(SessionContext ctx)
    {
        return (AbstractOrderEntry)getProperty(ctx, "orderEntry");
    }


    public AbstractOrderEntry getOrderEntry()
    {
        return getOrderEntry(getSession().getSessionContext());
    }


    public void setOrderEntry(SessionContext ctx, AbstractOrderEntry value)
    {
        setProperty(ctx, "orderEntry", value);
    }


    public void setOrderEntry(AbstractOrderEntry value)
    {
        setOrderEntry(getSession().getSessionContext(), value);
    }


    public Date getReachedDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "reachedDate");
    }


    public Date getReachedDate()
    {
        return getReachedDate(getSession().getSessionContext());
    }


    public void setReachedDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "reachedDate", value);
    }


    public void setReachedDate(Date value)
    {
        setReachedDate(getSession().getSessionContext(), value);
    }


    public Long getReceivedQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "receivedQuantity");
    }


    public Long getReceivedQuantity()
    {
        return getReceivedQuantity(getSession().getSessionContext());
    }


    public long getReceivedQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getReceivedQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getReceivedQuantityAsPrimitive()
    {
        return getReceivedQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setReceivedQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "receivedQuantity", value);
    }


    public void setReceivedQuantity(Long value)
    {
        setReceivedQuantity(getSession().getSessionContext(), value);
    }


    public void setReceivedQuantity(SessionContext ctx, long value)
    {
        setReceivedQuantity(ctx, Long.valueOf(value));
    }


    public void setReceivedQuantity(long value)
    {
        setReceivedQuantity(getSession().getSessionContext(), value);
    }


    public ReturnRequest getReturnRequest(SessionContext ctx)
    {
        return (ReturnRequest)getProperty(ctx, "returnRequest");
    }


    public ReturnRequest getReturnRequest()
    {
        return getReturnRequest(getSession().getSessionContext());
    }


    public void setReturnRequest(SessionContext ctx, ReturnRequest value)
    {
        RETURNREQUESTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setReturnRequest(ReturnRequest value)
    {
        setReturnRequest(getSession().getSessionContext(), value);
    }


    Integer getReturnRequestPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "returnRequestPOS");
    }


    Integer getReturnRequestPOS()
    {
        return getReturnRequestPOS(getSession().getSessionContext());
    }


    int getReturnRequestPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getReturnRequestPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getReturnRequestPOSAsPrimitive()
    {
        return getReturnRequestPOSAsPrimitive(getSession().getSessionContext());
    }


    void setReturnRequestPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "returnRequestPOS", value);
    }


    void setReturnRequestPOS(Integer value)
    {
        setReturnRequestPOS(getSession().getSessionContext(), value);
    }


    void setReturnRequestPOS(SessionContext ctx, int value)
    {
        setReturnRequestPOS(ctx, Integer.valueOf(value));
    }


    void setReturnRequestPOS(int value)
    {
        setReturnRequestPOS(getSession().getSessionContext(), value);
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


    public BigDecimal getTax(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "tax");
    }


    public BigDecimal getTax()
    {
        return getTax(getSession().getSessionContext());
    }


    public void setTax(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "tax", value);
    }


    public void setTax(BigDecimal value)
    {
        setTax(getSession().getSessionContext(), value);
    }
}
