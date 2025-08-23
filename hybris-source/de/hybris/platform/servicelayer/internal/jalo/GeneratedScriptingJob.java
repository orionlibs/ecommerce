package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedScriptingJob extends ServicelayerJob
{
    public static final String SCRIPTURI = "scriptURI";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("scriptURI", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getScriptURI(SessionContext ctx)
    {
        return (String)getProperty(ctx, "scriptURI");
    }


    public String getScriptURI()
    {
        return getScriptURI(getSession().getSessionContext());
    }


    public void setScriptURI(SessionContext ctx, String value)
    {
        setProperty(ctx, "scriptURI", value);
    }


    public void setScriptURI(String value)
    {
        setScriptURI(getSession().getSessionContext(), value);
    }
}
