package de.hybris.platform.deeplink.jalo.rules;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDeeplinkUrlRule extends GenericItem
{
    public static final String BASEURLPATTERN = "baseUrlPattern";
    public static final String DESTURLTEMPLATE = "destUrlTemplate";
    public static final String APPLICABLETYPE = "applicableType";
    public static final String USEFORWARD = "useForward";
    public static final String PRIORITY = "priority";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("baseUrlPattern", Item.AttributeMode.INITIAL);
        tmp.put("destUrlTemplate", Item.AttributeMode.INITIAL);
        tmp.put("applicableType", Item.AttributeMode.INITIAL);
        tmp.put("useForward", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ComposedType getApplicableType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "applicableType");
    }


    public ComposedType getApplicableType()
    {
        return getApplicableType(getSession().getSessionContext());
    }


    public void setApplicableType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "applicableType", value);
    }


    public void setApplicableType(ComposedType value)
    {
        setApplicableType(getSession().getSessionContext(), value);
    }


    public String getBaseUrlPattern(SessionContext ctx)
    {
        return (String)getProperty(ctx, "baseUrlPattern");
    }


    public String getBaseUrlPattern()
    {
        return getBaseUrlPattern(getSession().getSessionContext());
    }


    public void setBaseUrlPattern(SessionContext ctx, String value)
    {
        setProperty(ctx, "baseUrlPattern", value);
    }


    public void setBaseUrlPattern(String value)
    {
        setBaseUrlPattern(getSession().getSessionContext(), value);
    }


    public String getDestUrlTemplate(SessionContext ctx)
    {
        return (String)getProperty(ctx, "destUrlTemplate");
    }


    public String getDestUrlTemplate()
    {
        return getDestUrlTemplate(getSession().getSessionContext());
    }


    public void setDestUrlTemplate(SessionContext ctx, String value)
    {
        setProperty(ctx, "destUrlTemplate", value);
    }


    public void setDestUrlTemplate(String value)
    {
        setDestUrlTemplate(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public Boolean isUseForward(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useForward");
    }


    public Boolean isUseForward()
    {
        return isUseForward(getSession().getSessionContext());
    }


    public boolean isUseForwardAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseForward(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseForwardAsPrimitive()
    {
        return isUseForwardAsPrimitive(getSession().getSessionContext());
    }


    public void setUseForward(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useForward", value);
    }


    public void setUseForward(Boolean value)
    {
        setUseForward(getSession().getSessionContext(), value);
    }


    public void setUseForward(SessionContext ctx, boolean value)
    {
        setUseForward(ctx, Boolean.valueOf(value));
    }


    public void setUseForward(boolean value)
    {
        setUseForward(getSession().getSessionContext(), value);
    }
}
