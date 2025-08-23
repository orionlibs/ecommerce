package de.hybris.platform.jalo.media;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaFormatMapping extends GenericItem
{
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String MEDIACONTEXT = "mediaContext";
    protected static final BidirectionalOneToManyHandler<GeneratedMediaFormatMapping> MEDIACONTEXTHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.MEDIAFORMATMAPPING, false, "mediaContext", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("source", Item.AttributeMode.INITIAL);
        tmp.put("target", Item.AttributeMode.INITIAL);
        tmp.put("mediaContext", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MEDIACONTEXTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public MediaContext getMediaContext(SessionContext ctx)
    {
        return (MediaContext)getProperty(ctx, "mediaContext");
    }


    public MediaContext getMediaContext()
    {
        return getMediaContext(getSession().getSessionContext());
    }


    public void setMediaContext(SessionContext ctx, MediaContext value)
    {
        MEDIACONTEXTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setMediaContext(MediaContext value)
    {
        setMediaContext(getSession().getSessionContext(), value);
    }


    public MediaFormat getSource(SessionContext ctx)
    {
        return (MediaFormat)getProperty(ctx, "source");
    }


    public MediaFormat getSource()
    {
        return getSource(getSession().getSessionContext());
    }


    protected void setSource(SessionContext ctx, MediaFormat value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'source' is not changeable", 0);
        }
        setProperty(ctx, "source", value);
    }


    protected void setSource(MediaFormat value)
    {
        setSource(getSession().getSessionContext(), value);
    }


    public MediaFormat getTarget(SessionContext ctx)
    {
        return (MediaFormat)getProperty(ctx, "target");
    }


    public MediaFormat getTarget()
    {
        return getTarget(getSession().getSessionContext());
    }


    protected void setTarget(SessionContext ctx, MediaFormat value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'target' is not changeable", 0);
        }
        setProperty(ctx, "target", value);
    }


    protected void setTarget(MediaFormat value)
    {
        setTarget(getSession().getSessionContext(), value);
    }
}
