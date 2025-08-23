package de.hybris.platform.ticketsystem.events.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.user.Employee;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSessionEvent extends GenericItem
{
    public static final String EVENTTIME = "eventTime";
    public static final String AGENT = "agent";
    public static final String SESSIONID = "sessionID";
    public static final String BASESITE = "baseSite";
    public static final String GROUPS = "groups";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("eventTime", Item.AttributeMode.INITIAL);
        tmp.put("agent", Item.AttributeMode.INITIAL);
        tmp.put("sessionID", Item.AttributeMode.INITIAL);
        tmp.put("baseSite", Item.AttributeMode.INITIAL);
        tmp.put("groups", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Employee getAgent(SessionContext ctx)
    {
        return (Employee)getProperty(ctx, "agent");
    }


    public Employee getAgent()
    {
        return getAgent(getSession().getSessionContext());
    }


    public void setAgent(SessionContext ctx, Employee value)
    {
        setProperty(ctx, "agent", value);
    }


    public void setAgent(Employee value)
    {
        setAgent(getSession().getSessionContext(), value);
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


    public Date getEventTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "eventTime");
    }


    public Date getEventTime()
    {
        return getEventTime(getSession().getSessionContext());
    }


    public void setEventTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "eventTime", value);
    }


    public void setEventTime(Date value)
    {
        setEventTime(getSession().getSessionContext(), value);
    }


    public List<PrincipalGroup> getGroups(SessionContext ctx)
    {
        List<PrincipalGroup> coll = (List<PrincipalGroup>)getProperty(ctx, "groups");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<PrincipalGroup> getGroups()
    {
        return getGroups(getSession().getSessionContext());
    }


    public void setGroups(SessionContext ctx, List<PrincipalGroup> value)
    {
        setProperty(ctx, "groups", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setGroups(List<PrincipalGroup> value)
    {
        setGroups(getSession().getSessionContext(), value);
    }


    public String getSessionID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionID");
    }


    public String getSessionID()
    {
        return getSessionID(getSession().getSessionContext());
    }


    public void setSessionID(SessionContext ctx, String value)
    {
        setProperty(ctx, "sessionID", value);
    }


    public void setSessionID(String value)
    {
        setSessionID(getSession().getSessionContext(), value);
    }
}
