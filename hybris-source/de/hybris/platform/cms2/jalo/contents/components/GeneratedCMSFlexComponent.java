package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSFlexComponent extends SimpleCMSComponent
{
    public static final String FLEXTYPE = "flexType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("flexType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getFlexType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "flexType");
    }


    public String getFlexType()
    {
        return getFlexType(getSession().getSessionContext());
    }


    public void setFlexType(SessionContext ctx, String value)
    {
        setProperty(ctx, "flexType", value);
    }


    public void setFlexType(String value)
    {
        setFlexType(getSession().getSessionContext(), value);
    }
}
