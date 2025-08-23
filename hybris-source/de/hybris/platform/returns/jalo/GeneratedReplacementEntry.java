package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedReplacementEntry extends ReturnEntry
{
    public static final String REASON = "reason";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ReturnEntry.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("reason", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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
