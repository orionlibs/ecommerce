package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAttributeValueAssignment extends GenericItem
{
    public static final String VALUE = "value";
    public static final String ATTRIBUTEASSIGNMENT = "attributeAssignment";
    public static final String ATTRIBUTE = "attribute";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String POSITION = "position";
    public static final String EXTERNALID = "externalID";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("attributeAssignment", Item.AttributeMode.INITIAL);
        tmp.put("attribute", Item.AttributeMode.INITIAL);
        tmp.put("systemVersion", Item.AttributeMode.INITIAL);
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("externalID", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ClassificationAttribute getAttribute(SessionContext ctx)
    {
        return (ClassificationAttribute)getProperty(ctx, "attribute");
    }


    public ClassificationAttribute getAttribute()
    {
        return getAttribute(getSession().getSessionContext());
    }


    protected void setAttribute(SessionContext ctx, ClassificationAttribute value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'attribute' is not changeable", 0);
        }
        setProperty(ctx, "attribute", value);
    }


    protected void setAttribute(ClassificationAttribute value)
    {
        setAttribute(getSession().getSessionContext(), value);
    }


    public ClassAttributeAssignment getAttributeAssignment(SessionContext ctx)
    {
        return (ClassAttributeAssignment)getProperty(ctx, "attributeAssignment");
    }


    public ClassAttributeAssignment getAttributeAssignment()
    {
        return getAttributeAssignment(getSession().getSessionContext());
    }


    protected void setAttributeAssignment(SessionContext ctx, ClassAttributeAssignment value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'attributeAssignment' is not changeable", 0);
        }
        setProperty(ctx, "attributeAssignment", value);
    }


    protected void setAttributeAssignment(ClassAttributeAssignment value)
    {
        setAttributeAssignment(getSession().getSessionContext(), value);
    }


    public String getExternalID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalID");
    }


    public String getExternalID()
    {
        return getExternalID(getSession().getSessionContext());
    }


    public void setExternalID(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalID", value);
    }


    public void setExternalID(String value)
    {
        setExternalID(getSession().getSessionContext(), value);
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


    public ClassificationSystemVersion getSystemVersion(SessionContext ctx)
    {
        return (ClassificationSystemVersion)getProperty(ctx, "systemVersion");
    }


    public ClassificationSystemVersion getSystemVersion()
    {
        return getSystemVersion(getSession().getSessionContext());
    }


    protected void setSystemVersion(SessionContext ctx, ClassificationSystemVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'systemVersion' is not changeable", 0);
        }
        setProperty(ctx, "systemVersion", value);
    }


    protected void setSystemVersion(ClassificationSystemVersion value)
    {
        setSystemVersion(getSession().getSessionContext(), value);
    }


    public ClassificationAttributeValue getValue(SessionContext ctx)
    {
        return (ClassificationAttributeValue)getProperty(ctx, "value");
    }


    public ClassificationAttributeValue getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    protected void setValue(SessionContext ctx, ClassificationAttributeValue value)
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


    protected void setValue(ClassificationAttributeValue value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
