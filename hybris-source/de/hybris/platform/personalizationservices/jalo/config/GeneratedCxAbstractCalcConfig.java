package de.hybris.platform.personalizationservices.jalo.config;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCxAbstractCalcConfig extends GenericItem
{
    public static final String CODE = "code";
    public static final String ACTIONS = "actions";
    public static final String USERTYPE = "userType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("actions", Item.AttributeMode.INITIAL);
        tmp.put("userType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<String> getActions(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "actions");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "actions", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setActions(Set<String> value)
    {
        setActions(getSession().getSessionContext(), value);
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


    public EnumerationValue getUserType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "userType");
    }


    public EnumerationValue getUserType()
    {
        return getUserType(getSession().getSessionContext());
    }


    public void setUserType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "userType", value);
    }


    public void setUserType(EnumerationValue value)
    {
        setUserType(getSession().getSessionContext(), value);
    }
}
