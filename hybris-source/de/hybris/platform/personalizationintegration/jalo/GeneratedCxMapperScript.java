package de.hybris.platform.personalizationintegration.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.scripting.jalo.Script;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxMapperScript extends Script
{
    public static final String GROUP = "group";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Script.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("group", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "group");
    }


    public String getGroup()
    {
        return getGroup(getSession().getSessionContext());
    }


    public void setGroup(SessionContext ctx, String value)
    {
        setProperty(ctx, "group", value);
    }


    public void setGroup(String value)
    {
        setGroup(getSession().getSessionContext(), value);
    }
}
