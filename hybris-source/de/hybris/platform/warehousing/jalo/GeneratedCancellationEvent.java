package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCancellationEvent extends InventoryEvent
{
    public static final String REASON = "reason";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(InventoryEvent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("reason", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getReason(SessionContext ctx)
    {
        return (String)getProperty(ctx, "reason");
    }


    public String getReason()
    {
        return getReason(getSession().getSessionContext());
    }


    public void setReason(SessionContext ctx, String value)
    {
        setProperty(ctx, "reason", value);
    }


    public void setReason(String value)
    {
        setReason(getSession().getSessionContext(), value);
    }
}
