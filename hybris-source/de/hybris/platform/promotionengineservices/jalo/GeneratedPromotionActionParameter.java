package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionActionParameter extends GenericItem
{
    public static final String UUID = "uuid";
    public static final String VALUE = "value";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uuid", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getUuid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uuid");
    }


    public String getUuid()
    {
        return getUuid(getSession().getSessionContext());
    }


    public void setUuid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uuid", value);
    }


    public void setUuid(String value)
    {
        setUuid(getSession().getSessionContext(), value);
    }


    public Object getValue(SessionContext ctx)
    {
        return getProperty(ctx, "value");
    }


    public Object getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(Object value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
