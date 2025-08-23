package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCsTicketChangeEventEntry extends GenericItem
{
    public static final String ALTEREDATTRIBUTE = "alteredAttribute";
    public static final String OLDSTRINGVALUE = "oldStringValue";
    public static final String NEWSTRINGVALUE = "newStringValue";
    public static final String OLDBINARYVALUE = "oldBinaryValue";
    public static final String NEWBINARYVALUE = "newBinaryValue";
    public static final String EVENT = "event";
    protected static final BidirectionalOneToManyHandler<GeneratedCsTicketChangeEventEntry> EVENTHANDLER = new BidirectionalOneToManyHandler(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTENTRY, false, "event", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("alteredAttribute", Item.AttributeMode.INITIAL);
        tmp.put("oldStringValue", Item.AttributeMode.INITIAL);
        tmp.put("newStringValue", Item.AttributeMode.INITIAL);
        tmp.put("oldBinaryValue", Item.AttributeMode.INITIAL);
        tmp.put("newBinaryValue", Item.AttributeMode.INITIAL);
        tmp.put("event", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AttributeDescriptor getAlteredAttribute(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "alteredAttribute");
    }


    public AttributeDescriptor getAlteredAttribute()
    {
        return getAlteredAttribute(getSession().getSessionContext());
    }


    public void setAlteredAttribute(SessionContext ctx, AttributeDescriptor value)
    {
        setProperty(ctx, "alteredAttribute", value);
    }


    public void setAlteredAttribute(AttributeDescriptor value)
    {
        setAlteredAttribute(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        EVENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CsTicketEvent getEvent(SessionContext ctx)
    {
        return (CsTicketEvent)getProperty(ctx, "event");
    }


    public CsTicketEvent getEvent()
    {
        return getEvent(getSession().getSessionContext());
    }


    public void setEvent(SessionContext ctx, CsTicketEvent value)
    {
        EVENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setEvent(CsTicketEvent value)
    {
        setEvent(getSession().getSessionContext(), value);
    }


    public Object getNewBinaryValue(SessionContext ctx)
    {
        return getProperty(ctx, "newBinaryValue");
    }


    public Object getNewBinaryValue()
    {
        return getNewBinaryValue(getSession().getSessionContext());
    }


    public void setNewBinaryValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "newBinaryValue", value);
    }


    public void setNewBinaryValue(Object value)
    {
        setNewBinaryValue(getSession().getSessionContext(), value);
    }


    public String getNewStringValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "newStringValue");
    }


    public String getNewStringValue()
    {
        return getNewStringValue(getSession().getSessionContext());
    }


    public void setNewStringValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "newStringValue", value);
    }


    public void setNewStringValue(String value)
    {
        setNewStringValue(getSession().getSessionContext(), value);
    }


    public Object getOldBinaryValue(SessionContext ctx)
    {
        return getProperty(ctx, "oldBinaryValue");
    }


    public Object getOldBinaryValue()
    {
        return getOldBinaryValue(getSession().getSessionContext());
    }


    public void setOldBinaryValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "oldBinaryValue", value);
    }


    public void setOldBinaryValue(Object value)
    {
        setOldBinaryValue(getSession().getSessionContext(), value);
    }


    public String getOldStringValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "oldStringValue");
    }


    public String getOldStringValue()
    {
        return getOldStringValue(getSession().getSessionContext());
    }


    public void setOldStringValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "oldStringValue", value);
    }


    public void setOldStringValue(String value)
    {
        setOldStringValue(getSession().getSessionContext(), value);
    }
}
