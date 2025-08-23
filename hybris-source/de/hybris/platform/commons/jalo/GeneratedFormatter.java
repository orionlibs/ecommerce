package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFormatter extends Media
{
    public static final String OUTPUTMIMETYPE = "outputMimeType";
    public static final String SCRIPT = "script";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("outputMimeType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getOutputMimeType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "outputMimeType");
    }


    public String getOutputMimeType()
    {
        return getOutputMimeType(getSession().getSessionContext());
    }


    public void setOutputMimeType(SessionContext ctx, String value)
    {
        setProperty(ctx, "outputMimeType", value);
    }


    public void setOutputMimeType(String value)
    {
        setOutputMimeType(getSession().getSessionContext(), value);
    }


    public String getScript()
    {
        return getScript(getSession().getSessionContext());
    }


    public void setScript(String value)
    {
        setScript(getSession().getSessionContext(), value);
    }


    public abstract String getScript(SessionContext paramSessionContext);


    public abstract void setScript(SessionContext paramSessionContext, String paramString);
}
