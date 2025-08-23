package de.hybris.platform.ticketsystem.events.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSessionStartEvent extends SessionEvent
{
    public static final String CUSTOMER = "customer";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SessionEvent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("customer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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
}
