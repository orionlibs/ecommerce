package de.hybris.platform.jalo.cors;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCorsConfigurationProperty extends GenericItem
{
    public static final String CONTEXT = "context";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("context", Item.AttributeMode.INITIAL);
        tmp.put("key", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getContext(SessionContext ctx)
    {
        return (String)getProperty(ctx, "context");
    }


    public String getContext()
    {
        return getContext(getSession().getSessionContext());
    }


    protected void setContext(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'context' is not changeable", 0);
        }
        setProperty(ctx, "context", value);
    }


    protected void setContext(String value)
    {
        setContext(getSession().getSessionContext(), value);
    }


    public String getKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "key");
    }


    public String getKey()
    {
        return getKey(getSession().getSessionContext());
    }


    protected void setKey(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'key' is not changeable", 0);
        }
        setProperty(ctx, "key", value);
    }


    protected void setKey(String value)
    {
        setKey(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
