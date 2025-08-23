package de.hybris.platform.ordermodify.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.orderhistory.jalo.OrderHistoryEntry;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderModificationRecordEntry extends GenericItem
{
    public static final String CODE = "code";
    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ORIGINALVERSION = "originalVersion";
    public static final String PRINCIPAL = "principal";
    public static final String FAILEDMESSAGE = "failedMessage";
    public static final String NOTES = "notes";
    public static final String MODIFICATIONRECORD = "modificationRecord";
    public static final String ORDERENTRIESMODIFICATIONENTRIES = "orderEntriesModificationEntries";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderModificationRecordEntry> MODIFICATIONRECORDHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERMODIFICATIONRECORDENTRY, false, "modificationRecord", null, false, true, 0);
    protected static final OneToManyHandler<OrderEntryModificationRecordEntry> ORDERENTRIESMODIFICATIONENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERENTRYMODIFICATIONRECORDENTRY, false, "modificationRecordEntry", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("timestamp", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("originalVersion", Item.AttributeMode.INITIAL);
        tmp.put("principal", Item.AttributeMode.INITIAL);
        tmp.put("failedMessage", Item.AttributeMode.INITIAL);
        tmp.put("notes", Item.AttributeMode.INITIAL);
        tmp.put("modificationRecord", Item.AttributeMode.INITIAL);
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
        MODIFICATIONRECORDHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFailedMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "failedMessage");
    }


    public String getFailedMessage()
    {
        return getFailedMessage(getSession().getSessionContext());
    }


    public void setFailedMessage(SessionContext ctx, String value)
    {
        setProperty(ctx, "failedMessage", value);
    }


    public void setFailedMessage(String value)
    {
        setFailedMessage(getSession().getSessionContext(), value);
    }


    public OrderModificationRecord getModificationRecord(SessionContext ctx)
    {
        return (OrderModificationRecord)getProperty(ctx, "modificationRecord");
    }


    public OrderModificationRecord getModificationRecord()
    {
        return getModificationRecord(getSession().getSessionContext());
    }


    public void setModificationRecord(SessionContext ctx, OrderModificationRecord value)
    {
        MODIFICATIONRECORDHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setModificationRecord(OrderModificationRecord value)
    {
        setModificationRecord(getSession().getSessionContext(), value);
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


    public Collection<OrderEntryModificationRecordEntry> getOrderEntriesModificationEntries(SessionContext ctx)
    {
        return ORDERENTRIESMODIFICATIONENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<OrderEntryModificationRecordEntry> getOrderEntriesModificationEntries()
    {
        return getOrderEntriesModificationEntries(getSession().getSessionContext());
    }


    public void setOrderEntriesModificationEntries(SessionContext ctx, Collection<OrderEntryModificationRecordEntry> value)
    {
        ORDERENTRIESMODIFICATIONENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setOrderEntriesModificationEntries(Collection<OrderEntryModificationRecordEntry> value)
    {
        setOrderEntriesModificationEntries(getSession().getSessionContext(), value);
    }


    public void addToOrderEntriesModificationEntries(SessionContext ctx, OrderEntryModificationRecordEntry value)
    {
        ORDERENTRIESMODIFICATIONENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToOrderEntriesModificationEntries(OrderEntryModificationRecordEntry value)
    {
        addToOrderEntriesModificationEntries(getSession().getSessionContext(), value);
    }


    public void removeFromOrderEntriesModificationEntries(SessionContext ctx, OrderEntryModificationRecordEntry value)
    {
        ORDERENTRIESMODIFICATIONENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromOrderEntriesModificationEntries(OrderEntryModificationRecordEntry value)
    {
        removeFromOrderEntriesModificationEntries(getSession().getSessionContext(), value);
    }


    public OrderHistoryEntry getOriginalVersion(SessionContext ctx)
    {
        return (OrderHistoryEntry)getProperty(ctx, "originalVersion");
    }


    public OrderHistoryEntry getOriginalVersion()
    {
        return getOriginalVersion(getSession().getSessionContext());
    }


    protected void setOriginalVersion(SessionContext ctx, OrderHistoryEntry value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'originalVersion' is not changeable", 0);
        }
        setProperty(ctx, "originalVersion", value);
    }


    protected void setOriginalVersion(OrderHistoryEntry value)
    {
        setOriginalVersion(getSession().getSessionContext(), value);
    }


    public Principal getPrincipal(SessionContext ctx)
    {
        return (Principal)getProperty(ctx, "principal");
    }


    public Principal getPrincipal()
    {
        return getPrincipal(getSession().getSessionContext());
    }


    public void setPrincipal(SessionContext ctx, Principal value)
    {
        setProperty(ctx, "principal", value);
    }


    public void setPrincipal(Principal value)
    {
        setPrincipal(getSession().getSessionContext(), value);
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


    public Date getTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "timestamp");
    }


    public Date getTimestamp()
    {
        return getTimestamp(getSession().getSessionContext());
    }


    protected void setTimestamp(SessionContext ctx, Date value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'timestamp' is not changeable", 0);
        }
        setProperty(ctx, "timestamp", value);
    }


    protected void setTimestamp(Date value)
    {
        setTimestamp(getSession().getSessionContext(), value);
    }
}
