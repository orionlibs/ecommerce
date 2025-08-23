package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.mediaconversion.constants.GeneratedMediaConversionConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaMetaData extends GenericItem
{
    public static final String CODE = "code";
    public static final String VALUE = "value";
    public static final String GROUPNAME = "groupName";
    public static final String MEDIA = "media";
    protected static final BidirectionalOneToManyHandler<GeneratedMediaMetaData> MEDIAHANDLER = new BidirectionalOneToManyHandler(GeneratedMediaConversionConstants.TC.MEDIAMETADATA, false, "media", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("groupName", Item.AttributeMode.INITIAL);
        tmp.put("media", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MEDIAHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getGroupName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "groupName");
    }


    public String getGroupName()
    {
        return getGroupName(getSession().getSessionContext());
    }


    protected void setGroupName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'groupName' is not changeable", 0);
        }
        setProperty(ctx, "groupName", value);
    }


    protected void setGroupName(String value)
    {
        setGroupName(getSession().getSessionContext(), value);
    }


    public Media getMedia(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "media");
    }


    public Media getMedia()
    {
        return getMedia(getSession().getSessionContext());
    }


    protected void setMedia(SessionContext ctx, Media value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'media' is not changeable", 0);
        }
        MEDIAHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setMedia(Media value)
    {
        setMedia(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    protected void setValue(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'value' is not changeable", 0);
        }
        setProperty(ctx, "value", value);
    }


    protected void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
