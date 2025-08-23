package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCsTicketEmail extends GenericItem
{
    public static final String MESSAGEID = "messageId";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String TICKETPOS = "ticketPOS";
    public static final String TICKET = "ticket";
    protected static final BidirectionalOneToManyHandler<GeneratedCsTicketEmail> TICKETHANDLER = new BidirectionalOneToManyHandler(GeneratedTicketsystemConstants.TC.CSTICKETEMAIL, false, "ticket", "ticketPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("messageId", Item.AttributeMode.INITIAL);
        tmp.put("from", Item.AttributeMode.INITIAL);
        tmp.put("to", Item.AttributeMode.INITIAL);
        tmp.put("subject", Item.AttributeMode.INITIAL);
        tmp.put("body", Item.AttributeMode.INITIAL);
        tmp.put("ticketPOS", Item.AttributeMode.INITIAL);
        tmp.put("ticket", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getBody(SessionContext ctx)
    {
        return (String)getProperty(ctx, "body");
    }


    public String getBody()
    {
        return getBody(getSession().getSessionContext());
    }


    public void setBody(SessionContext ctx, String value)
    {
        setProperty(ctx, "body", value);
    }


    public void setBody(String value)
    {
        setBody(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        TICKETHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFrom(SessionContext ctx)
    {
        return (String)getProperty(ctx, "from");
    }


    public String getFrom()
    {
        return getFrom(getSession().getSessionContext());
    }


    public void setFrom(SessionContext ctx, String value)
    {
        setProperty(ctx, "from", value);
    }


    public void setFrom(String value)
    {
        setFrom(getSession().getSessionContext(), value);
    }


    public String getMessageId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "messageId");
    }


    public String getMessageId()
    {
        return getMessageId(getSession().getSessionContext());
    }


    public void setMessageId(SessionContext ctx, String value)
    {
        setProperty(ctx, "messageId", value);
    }


    public void setMessageId(String value)
    {
        setMessageId(getSession().getSessionContext(), value);
    }


    public String getSubject(SessionContext ctx)
    {
        return (String)getProperty(ctx, "subject");
    }


    public String getSubject()
    {
        return getSubject(getSession().getSessionContext());
    }


    public void setSubject(SessionContext ctx, String value)
    {
        setProperty(ctx, "subject", value);
    }


    public void setSubject(String value)
    {
        setSubject(getSession().getSessionContext(), value);
    }


    public CsTicketEvent getTicket(SessionContext ctx)
    {
        return (CsTicketEvent)getProperty(ctx, "ticket");
    }


    public CsTicketEvent getTicket()
    {
        return getTicket(getSession().getSessionContext());
    }


    public void setTicket(SessionContext ctx, CsTicketEvent value)
    {
        TICKETHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setTicket(CsTicketEvent value)
    {
        setTicket(getSession().getSessionContext(), value);
    }


    Integer getTicketPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "ticketPOS");
    }


    Integer getTicketPOS()
    {
        return getTicketPOS(getSession().getSessionContext());
    }


    int getTicketPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getTicketPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getTicketPOSAsPrimitive()
    {
        return getTicketPOSAsPrimitive(getSession().getSessionContext());
    }


    void setTicketPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "ticketPOS", value);
    }


    void setTicketPOS(Integer value)
    {
        setTicketPOS(getSession().getSessionContext(), value);
    }


    void setTicketPOS(SessionContext ctx, int value)
    {
        setTicketPOS(ctx, Integer.valueOf(value));
    }


    void setTicketPOS(int value)
    {
        setTicketPOS(getSession().getSessionContext(), value);
    }


    public String getTo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "to");
    }


    public String getTo()
    {
        return getTo(getSession().getSessionContext());
    }


    public void setTo(SessionContext ctx, String value)
    {
        setProperty(ctx, "to", value);
    }


    public void setTo(String value)
    {
        setTo(getSession().getSessionContext(), value);
    }
}
