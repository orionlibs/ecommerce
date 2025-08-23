package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSSiteContextComponent extends SimpleCMSComponent
{
    public static final String CONTEXT = "context";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("context", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getContext(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "context");
    }


    public EnumerationValue getContext()
    {
        return getContext(getSession().getSessionContext());
    }


    public void setContext(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "context", value);
    }


    public void setContext(EnumerationValue value)
    {
        setContext(getSession().getSessionContext(), value);
    }
}
