package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedURLCronJob extends MediaProcessCronJob
{
    public static final String URL = "URL";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MediaProcessCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("URL", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getURL(SessionContext ctx)
    {
        return (String)getProperty(ctx, "URL");
    }


    public String getURL()
    {
        return getURL(getSession().getSessionContext());
    }


    public void setURL(SessionContext ctx, String value)
    {
        setProperty(ctx, "URL", value);
    }


    public void setURL(String value)
    {
        setURL(getSession().getSessionContext(), value);
    }
}
