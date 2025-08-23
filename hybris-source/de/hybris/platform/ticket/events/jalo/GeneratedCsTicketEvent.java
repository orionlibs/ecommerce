package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.comments.jalo.Comment;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.ticket.jalo.CsTicket;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCsTicketEvent extends Comment
{
    public static final String STARTDATETIME = "startDateTime";
    public static final String ENDDATETIME = "endDateTime";
    public static final String TICKET = "ticket";
    public static final String EMAILS = "emails";
    public static final String ENTRIES = "entries";
    protected static final OneToManyHandler<CsTicketEmail> EMAILSHANDLER = new OneToManyHandler(GeneratedTicketsystemConstants.TC.CSTICKETEMAIL, false, "ticket", "ticketPOS", true, true, 2);
    protected static final OneToManyHandler<CsTicketChangeEventEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTENTRY, false, "event", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Comment.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("startDateTime", Item.AttributeMode.INITIAL);
        tmp.put("endDateTime", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CsTicketEmail> getEmails(SessionContext ctx)
    {
        return (List<CsTicketEmail>)EMAILSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CsTicketEmail> getEmails()
    {
        return getEmails(getSession().getSessionContext());
    }


    public void setEmails(SessionContext ctx, List<CsTicketEmail> value)
    {
        EMAILSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEmails(List<CsTicketEmail> value)
    {
        setEmails(getSession().getSessionContext(), value);
    }


    public void addToEmails(SessionContext ctx, CsTicketEmail value)
    {
        EMAILSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEmails(CsTicketEmail value)
    {
        addToEmails(getSession().getSessionContext(), value);
    }


    public void removeFromEmails(SessionContext ctx, CsTicketEmail value)
    {
        EMAILSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEmails(CsTicketEmail value)
    {
        removeFromEmails(getSession().getSessionContext(), value);
    }


    public Date getEndDateTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endDateTime");
    }


    public Date getEndDateTime()
    {
        return getEndDateTime(getSession().getSessionContext());
    }


    public void setEndDateTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endDateTime", value);
    }


    public void setEndDateTime(Date value)
    {
        setEndDateTime(getSession().getSessionContext(), value);
    }


    public Set<CsTicketChangeEventEntry> getEntries(SessionContext ctx)
    {
        return (Set<CsTicketChangeEventEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<CsTicketChangeEventEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, Set<CsTicketChangeEventEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(Set<CsTicketChangeEventEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, CsTicketChangeEventEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(CsTicketChangeEventEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, CsTicketChangeEventEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(CsTicketChangeEventEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    public Date getStartDateTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startDateTime");
    }


    public Date getStartDateTime()
    {
        return getStartDateTime(getSession().getSessionContext());
    }


    public void setStartDateTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startDateTime", value);
    }


    public void setStartDateTime(Date value)
    {
        setStartDateTime(getSession().getSessionContext(), value);
    }


    public CsTicket getTicket()
    {
        return getTicket(getSession().getSessionContext());
    }


    public void setTicket(CsTicket value)
    {
        setTicket(getSession().getSessionContext(), value);
    }


    public abstract CsTicket getTicket(SessionContext paramSessionContext);


    public abstract void setTicket(SessionContext paramSessionContext, CsTicket paramCsTicket);
}
