package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBruteForceLoginAttempts extends GenericItem
{
    public static final String UID = "uid";
    public static final String ATTEMPTS = "attempts";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("attempts", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAttempts(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "attempts");
    }


    public Integer getAttempts()
    {
        return getAttempts(getSession().getSessionContext());
    }


    public int getAttemptsAsPrimitive(SessionContext ctx)
    {
        Integer value = getAttempts(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAttemptsAsPrimitive()
    {
        return getAttemptsAsPrimitive(getSession().getSessionContext());
    }


    public void setAttempts(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "attempts", value);
    }


    public void setAttempts(Integer value)
    {
        setAttempts(getSession().getSessionContext(), value);
    }


    public void setAttempts(SessionContext ctx, int value)
    {
        setAttempts(ctx, Integer.valueOf(value));
    }


    public void setAttempts(int value)
    {
        setAttempts(getSession().getSessionContext(), value);
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    protected void setUid(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'uid' is not changeable", 0);
        }
        setProperty(ctx, "uid", value);
    }


    protected void setUid(String value)
    {
        setUid(getSession().getSessionContext(), value);
    }
}
