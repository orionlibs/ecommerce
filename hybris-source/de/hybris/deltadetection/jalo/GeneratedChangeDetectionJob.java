package de.hybris.deltadetection.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedChangeDetectionJob extends ServicelayerJob
{
    public static final String TYPEPK = "typePK";
    public static final String STREAMID = "streamId";
    public static final String OUTPUT = "output";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("typePK", Item.AttributeMode.INITIAL);
        tmp.put("streamId", Item.AttributeMode.INITIAL);
        tmp.put("output", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getOutput(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "output");
    }


    public Media getOutput()
    {
        return getOutput(getSession().getSessionContext());
    }


    public void setOutput(SessionContext ctx, Media value)
    {
        setProperty(ctx, "output", value);
    }


    public void setOutput(Media value)
    {
        setOutput(getSession().getSessionContext(), value);
    }


    public String getStreamId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "streamId");
    }


    public String getStreamId()
    {
        return getStreamId(getSession().getSessionContext());
    }


    public void setStreamId(SessionContext ctx, String value)
    {
        setProperty(ctx, "streamId", value);
    }


    public void setStreamId(String value)
    {
        setStreamId(getSession().getSessionContext(), value);
    }


    public ComposedType getTypePK(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "typePK");
    }


    public ComposedType getTypePK()
    {
        return getTypePK(getSession().getSessionContext());
    }


    public void setTypePK(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "typePK", value);
    }


    public void setTypePK(ComposedType value)
    {
        setTypePK(getSession().getSessionContext(), value);
    }
}
