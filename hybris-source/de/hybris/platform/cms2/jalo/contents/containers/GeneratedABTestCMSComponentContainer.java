package de.hybris.platform.cms2.jalo.contents.containers;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedABTestCMSComponentContainer extends AbstractCMSComponentContainer
{
    public static final String SCOPE = "scope";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCMSComponentContainer.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("scope", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getScope(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "scope");
    }


    public EnumerationValue getScope()
    {
        return getScope(getSession().getSessionContext());
    }


    public void setScope(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "scope", value);
    }


    public void setScope(EnumerationValue value)
    {
        setScope(getSession().getSessionContext(), value);
    }
}
