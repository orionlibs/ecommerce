package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCsCustomerEvent extends CsTicketEvent
{
    public static final String INTERVENTIONTYPE = "interventionType";
    public static final String REASON = "reason";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CsTicketEvent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("interventionType", Item.AttributeMode.INITIAL);
        tmp.put("reason", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getInterventionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "interventionType");
    }


    public EnumerationValue getInterventionType()
    {
        return getInterventionType(getSession().getSessionContext());
    }


    public void setInterventionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "interventionType", value);
    }


    public void setInterventionType(EnumerationValue value)
    {
        setInterventionType(getSession().getSessionContext(), value);
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
