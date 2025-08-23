package de.hybris.deltadetection.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractChangeProcessorJob extends ServicelayerJob
{
    public static final String INPUT = "input";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("input", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getInput(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "input");
    }


    public Media getInput()
    {
        return getInput(getSession().getSessionContext());
    }


    public void setInput(SessionContext ctx, Media value)
    {
        setProperty(ctx, "input", value);
    }


    public void setInput(Media value)
    {
        setInput(getSession().getSessionContext(), value);
    }
}
