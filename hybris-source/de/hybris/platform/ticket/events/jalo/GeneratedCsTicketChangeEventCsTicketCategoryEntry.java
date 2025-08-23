package de.hybris.platform.ticket.events.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCsTicketChangeEventCsTicketCategoryEntry extends CsTicketChangeEventEntry
{
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CsTicketChangeEventEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("oldValue", Item.AttributeMode.INITIAL);
        tmp.put("newValue", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getNewValue(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "newValue");
    }


    public EnumerationValue getNewValue()
    {
        return getNewValue(getSession().getSessionContext());
    }


    public void setNewValue(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "newValue", value);
    }


    public void setNewValue(EnumerationValue value)
    {
        setNewValue(getSession().getSessionContext(), value);
    }


    public EnumerationValue getOldValue(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "oldValue");
    }


    public EnumerationValue getOldValue()
    {
        return getOldValue(getSession().getSessionContext());
    }


    public void setOldValue(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "oldValue", value);
    }


    public void setOldValue(EnumerationValue value)
    {
        setOldValue(getSession().getSessionContext(), value);
    }
}
