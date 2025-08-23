package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomer extends User
{
    public static final String CUSTOMERID = "customerID";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(User.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("customerID", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCustomerID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "customerID");
    }


    public String getCustomerID()
    {
        return getCustomerID(getSession().getSessionContext());
    }


    public void setCustomerID(SessionContext ctx, String value)
    {
        setProperty(ctx, "customerID", value);
    }


    public void setCustomerID(String value)
    {
        setCustomerID(getSession().getSessionContext(), value);
    }
}
