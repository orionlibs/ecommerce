package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractAsSearchConfiguration extends AbstractAsConfiguration
{
    public static final String UNIQUEIDX = "uniqueIdx";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("uniqueIdx", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getUniqueIdx(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uniqueIdx");
    }


    public String getUniqueIdx()
    {
        return getUniqueIdx(getSession().getSessionContext());
    }


    public void setUniqueIdx(SessionContext ctx, String value)
    {
        setProperty(ctx, "uniqueIdx", value);
    }


    public void setUniqueIdx(String value)
    {
        setUniqueIdx(getSession().getSessionContext(), value);
    }
}
