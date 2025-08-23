package com.hybris.backoffice.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomTheme extends Theme
{
    public static final String BASE = "base";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Theme.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("base", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Theme getBase(SessionContext ctx)
    {
        return (Theme)getProperty(ctx, "base");
    }


    public Theme getBase()
    {
        return getBase(getSession().getSessionContext());
    }


    public void setBase(SessionContext ctx, Theme value)
    {
        setProperty(ctx, "base", value);
    }


    public void setBase(Theme value)
    {
        setBase(getSession().getSessionContext(), value);
    }
}
