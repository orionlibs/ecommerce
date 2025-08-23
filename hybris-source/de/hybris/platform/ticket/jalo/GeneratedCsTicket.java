package de.hybris.platform.ticket.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.ticket.events.jalo.CsTicketEvent;
import de.hybris.platform.ticket.events.jalo.CsTicketResolutionEvent;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCsTicket extends GenericItem
{
    public static final String TICKETID = "ticketID";
    public static final String CUSTOMER = "customer";
    public static final String ORDER = "order";
    public static final String HEADLINE = "headline";
    public static final String CATEGORY = "category";
    public static final String PRIORITY = "priority";
    public static final String STATE = "state";
    public static final String ASSIGNEDAGENT = "assignedAgent";
    public static final String ASSIGNEDGROUP = "assignedGroup";
    public static final String RESOLUTION = "resolution";
    public static final String BASESITE = "baseSite";
    public static final String EVENTS = "events";
    public static final String RETENTIONDATE = "retentionDate";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("ticketID", Item.AttributeMode.INITIAL);
        tmp.put("customer", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("headline", Item.AttributeMode.INITIAL);
        tmp.put("category", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("state", Item.AttributeMode.INITIAL);
        tmp.put("assignedAgent", Item.AttributeMode.INITIAL);
        tmp.put("assignedGroup", Item.AttributeMode.INITIAL);
        tmp.put("resolution", Item.AttributeMode.INITIAL);
        tmp.put("baseSite", Item.AttributeMode.INITIAL);
        tmp.put("retentionDate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Employee getAssignedAgent(SessionContext ctx)
    {
        return (Employee)getProperty(ctx, "assignedAgent");
    }


    public Employee getAssignedAgent()
    {
        return getAssignedAgent(getSession().getSessionContext());
    }


    public void setAssignedAgent(SessionContext ctx, Employee value)
    {
        setProperty(ctx, "assignedAgent", value);
    }


    public void setAssignedAgent(Employee value)
    {
        setAssignedAgent(getSession().getSessionContext(), value);
    }


    public CsAgentGroup getAssignedGroup(SessionContext ctx)
    {
        return (CsAgentGroup)getProperty(ctx, "assignedGroup");
    }


    public CsAgentGroup getAssignedGroup()
    {
        return getAssignedGroup(getSession().getSessionContext());
    }


    public void setAssignedGroup(SessionContext ctx, CsAgentGroup value)
    {
        setProperty(ctx, "assignedGroup", value);
    }


    public void setAssignedGroup(CsAgentGroup value)
    {
        setAssignedGroup(getSession().getSessionContext(), value);
    }


    public BaseSite getBaseSite(SessionContext ctx)
    {
        return (BaseSite)getProperty(ctx, "baseSite");
    }


    public BaseSite getBaseSite()
    {
        return getBaseSite(getSession().getSessionContext());
    }


    public void setBaseSite(SessionContext ctx, BaseSite value)
    {
        setProperty(ctx, "baseSite", value);
    }


    public void setBaseSite(BaseSite value)
    {
        setBaseSite(getSession().getSessionContext(), value);
    }


    public EnumerationValue getCategory(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "category");
    }


    public EnumerationValue getCategory()
    {
        return getCategory(getSession().getSessionContext());
    }


    public void setCategory(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "category", value);
    }


    public void setCategory(EnumerationValue value)
    {
        setCategory(getSession().getSessionContext(), value);
    }


    public User getCustomer(SessionContext ctx)
    {
        return (User)getProperty(ctx, "customer");
    }


    public User getCustomer()
    {
        return getCustomer(getSession().getSessionContext());
    }


    public void setCustomer(SessionContext ctx, User value)
    {
        setProperty(ctx, "customer", value);
    }


    public void setCustomer(User value)
    {
        setCustomer(getSession().getSessionContext(), value);
    }


    public List<CsTicketEvent> getEvents()
    {
        return getEvents(getSession().getSessionContext());
    }


    public String getHeadline(SessionContext ctx)
    {
        return (String)getProperty(ctx, "headline");
    }


    public String getHeadline()
    {
        return getHeadline(getSession().getSessionContext());
    }


    public void setHeadline(SessionContext ctx, String value)
    {
        setProperty(ctx, "headline", value);
    }


    public void setHeadline(String value)
    {
        setHeadline(getSession().getSessionContext(), value);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)getProperty(ctx, "order");
    }


    public AbstractOrder getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, AbstractOrder value)
    {
        setProperty(ctx, "order", value);
    }


    public void setOrder(AbstractOrder value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public EnumerationValue getPriority(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "priority");
    }


    public EnumerationValue getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(EnumerationValue value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public CsTicketResolutionEvent getResolution(SessionContext ctx)
    {
        return (CsTicketResolutionEvent)getProperty(ctx, "resolution");
    }


    public CsTicketResolutionEvent getResolution()
    {
        return getResolution(getSession().getSessionContext());
    }


    public void setResolution(SessionContext ctx, CsTicketResolutionEvent value)
    {
        setProperty(ctx, "resolution", value);
    }


    public void setResolution(CsTicketResolutionEvent value)
    {
        setResolution(getSession().getSessionContext(), value);
    }


    public Date getRetentionDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "retentionDate");
    }


    public Date getRetentionDate()
    {
        return getRetentionDate(getSession().getSessionContext());
    }


    public void setRetentionDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "retentionDate", value);
    }


    public void setRetentionDate(Date value)
    {
        setRetentionDate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "state");
    }


    public EnumerationValue getState()
    {
        return getState(getSession().getSessionContext());
    }


    public void setState(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "state", value);
    }


    public void setState(EnumerationValue value)
    {
        setState(getSession().getSessionContext(), value);
    }


    public String getTicketID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ticketID");
    }


    public String getTicketID()
    {
        return getTicketID(getSession().getSessionContext());
    }


    protected void setTicketID(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'ticketID' is not changeable", 0);
        }
        setProperty(ctx, "ticketID", value);
    }


    protected void setTicketID(String value)
    {
        setTicketID(getSession().getSessionContext(), value);
    }


    public abstract List<CsTicketEvent> getEvents(SessionContext paramSessionContext);
}
