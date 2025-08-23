package de.hybris.y2ysync.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.y2ysync.constants.GeneratedY2ysyncConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2YColumnDefintion extends GenericItem
{
    public static final String POSITION = "position";
    public static final String ATTRIBUTEDESCRIPTOR = "attributeDescriptor";
    public static final String LANGUAGE = "language";
    public static final String COLUMNNAME = "columnName";
    public static final String IMPEXHEADER = "impexHeader";
    public static final String STREAMCONFIGURATION = "streamConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedY2YColumnDefintion> STREAMCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedY2ysyncConstants.TC.Y2YCOLUMNDEFINITION, false, "streamConfiguration", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("attributeDescriptor", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("columnName", Item.AttributeMode.INITIAL);
        tmp.put("impexHeader", Item.AttributeMode.INITIAL);
        tmp.put("streamConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AttributeDescriptor getAttributeDescriptor(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "attributeDescriptor");
    }


    public AttributeDescriptor getAttributeDescriptor()
    {
        return getAttributeDescriptor(getSession().getSessionContext());
    }


    protected void setAttributeDescriptor(SessionContext ctx, AttributeDescriptor value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'attributeDescriptor' is not changeable", 0);
        }
        setProperty(ctx, "attributeDescriptor", value);
    }


    protected void setAttributeDescriptor(AttributeDescriptor value)
    {
        setAttributeDescriptor(getSession().getSessionContext(), value);
    }


    public String getColumnName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "columnName");
    }


    public String getColumnName()
    {
        return getColumnName(getSession().getSessionContext());
    }


    public void setColumnName(SessionContext ctx, String value)
    {
        setProperty(ctx, "columnName", value);
    }


    public void setColumnName(String value)
    {
        setColumnName(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        STREAMCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getImpexHeader(SessionContext ctx)
    {
        return (String)getProperty(ctx, "impexHeader");
    }


    public String getImpexHeader()
    {
        return getImpexHeader(getSession().getSessionContext());
    }


    public void setImpexHeader(SessionContext ctx, String value)
    {
        setProperty(ctx, "impexHeader", value);
    }


    public void setImpexHeader(String value)
    {
        setImpexHeader(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public Integer getPosition(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "position");
    }


    public Integer getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public int getPositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getPosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPositionAsPrimitive()
    {
        return getPositionAsPrimitive(getSession().getSessionContext());
    }


    public void setPosition(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "position", value);
    }


    public void setPosition(Integer value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public void setPosition(SessionContext ctx, int value)
    {
        setPosition(ctx, Integer.valueOf(value));
    }


    public void setPosition(int value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public Y2YStreamConfiguration getStreamConfiguration(SessionContext ctx)
    {
        return (Y2YStreamConfiguration)getProperty(ctx, "streamConfiguration");
    }


    public Y2YStreamConfiguration getStreamConfiguration()
    {
        return getStreamConfiguration(getSession().getSessionContext());
    }


    protected void setStreamConfiguration(SessionContext ctx, Y2YStreamConfiguration value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'streamConfiguration' is not changeable", 0);
        }
        STREAMCONFIGURATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setStreamConfiguration(Y2YStreamConfiguration value)
    {
        setStreamConfiguration(getSession().getSessionContext(), value);
    }
}
