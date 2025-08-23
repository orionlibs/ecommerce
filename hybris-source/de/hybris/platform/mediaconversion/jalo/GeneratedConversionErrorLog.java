package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.mediaconversion.constants.GeneratedMediaConversionConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedConversionErrorLog extends GenericItem
{
    public static final String TARGETFORMAT = "targetFormat";
    public static final String SOURCEMEDIA = "sourceMedia";
    public static final String ERRORMESSAGE = "errorMessage";
    public static final String CONTAINER = "container";
    protected static final BidirectionalOneToManyHandler<GeneratedConversionErrorLog> CONTAINERHANDLER = new BidirectionalOneToManyHandler(GeneratedMediaConversionConstants.TC.CONVERSIONERRORLOG, false, "container", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("targetFormat", Item.AttributeMode.INITIAL);
        tmp.put("sourceMedia", Item.AttributeMode.INITIAL);
        tmp.put("errorMessage", Item.AttributeMode.INITIAL);
        tmp.put("container", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public MediaContainer getContainer(SessionContext ctx)
    {
        return (MediaContainer)getProperty(ctx, "container");
    }


    public MediaContainer getContainer()
    {
        return getContainer(getSession().getSessionContext());
    }


    protected void setContainer(SessionContext ctx, MediaContainer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'container' is not changeable", 0);
        }
        CONTAINERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setContainer(MediaContainer value)
    {
        setContainer(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONTAINERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getErrorMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "errorMessage");
    }


    public String getErrorMessage()
    {
        return getErrorMessage(getSession().getSessionContext());
    }


    protected void setErrorMessage(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'errorMessage' is not changeable", 0);
        }
        setProperty(ctx, "errorMessage", value);
    }


    protected void setErrorMessage(String value)
    {
        setErrorMessage(getSession().getSessionContext(), value);
    }


    public Media getSourceMedia(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "sourceMedia");
    }


    public Media getSourceMedia()
    {
        return getSourceMedia(getSession().getSessionContext());
    }


    protected void setSourceMedia(SessionContext ctx, Media value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceMedia' is not changeable", 0);
        }
        setProperty(ctx, "sourceMedia", value);
    }


    protected void setSourceMedia(Media value)
    {
        setSourceMedia(getSession().getSessionContext(), value);
    }


    public ConversionMediaFormat getTargetFormat(SessionContext ctx)
    {
        return (ConversionMediaFormat)getProperty(ctx, "targetFormat");
    }


    public ConversionMediaFormat getTargetFormat()
    {
        return getTargetFormat(getSession().getSessionContext());
    }


    protected void setTargetFormat(SessionContext ctx, ConversionMediaFormat value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetFormat' is not changeable", 0);
        }
        setProperty(ctx, "targetFormat", value);
    }


    protected void setTargetFormat(ConversionMediaFormat value)
    {
        setTargetFormat(getSession().getSessionContext(), value);
    }
}
