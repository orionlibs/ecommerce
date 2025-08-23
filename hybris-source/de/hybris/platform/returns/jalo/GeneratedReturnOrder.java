package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Order;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedReturnOrder extends Order
{
    public static final String FULFILMENTSTATUS = "fulfilmentStatus";
    public static final String NOTES = "notes";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Order.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("fulfilmentStatus", Item.AttributeMode.INITIAL);
        tmp.put("notes", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getFulfilmentStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "fulfilmentStatus");
    }


    public EnumerationValue getFulfilmentStatus()
    {
        return getFulfilmentStatus(getSession().getSessionContext());
    }


    public void setFulfilmentStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "fulfilmentStatus", value);
    }


    public void setFulfilmentStatus(EnumerationValue value)
    {
        setFulfilmentStatus(getSession().getSessionContext(), value);
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
}
