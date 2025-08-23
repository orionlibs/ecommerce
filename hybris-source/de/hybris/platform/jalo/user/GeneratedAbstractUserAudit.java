package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractUserAudit extends GenericItem
{
    public static final String UID = "uid";
    public static final String USERPK = "userPK";
    public static final String CHANGINGUSER = "changingUser";
    public static final String CHANGINGAPPLICATION = "changingApplication";
    public static final String IPADDRESS = "ipAddress";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("userPK", Item.AttributeMode.INITIAL);
        tmp.put("changingUser", Item.AttributeMode.INITIAL);
        tmp.put("changingApplication", Item.AttributeMode.INITIAL);
        tmp.put("ipAddress", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getChangingApplication(SessionContext ctx)
    {
        return (String)getProperty(ctx, "changingApplication");
    }


    public String getChangingApplication()
    {
        return getChangingApplication(getSession().getSessionContext());
    }


    public void setChangingApplication(SessionContext ctx, String value)
    {
        setProperty(ctx, "changingApplication", value);
    }


    public void setChangingApplication(String value)
    {
        setChangingApplication(getSession().getSessionContext(), value);
    }


    public String getChangingUser(SessionContext ctx)
    {
        return (String)getProperty(ctx, "changingUser");
    }


    public String getChangingUser()
    {
        return getChangingUser(getSession().getSessionContext());
    }


    public void setChangingUser(SessionContext ctx, String value)
    {
        setProperty(ctx, "changingUser", value);
    }


    public void setChangingUser(String value)
    {
        setChangingUser(getSession().getSessionContext(), value);
    }


    public String getIpAddress(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ipAddress");
    }


    public String getIpAddress()
    {
        return getIpAddress(getSession().getSessionContext());
    }


    protected void setIpAddress(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'ipAddress' is not changeable", 0);
        }
        setProperty(ctx, "ipAddress", value);
    }


    protected void setIpAddress(String value)
    {
        setIpAddress(getSession().getSessionContext(), value);
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


    public Long getUserPK(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "userPK");
    }


    public Long getUserPK()
    {
        return getUserPK(getSession().getSessionContext());
    }


    public long getUserPKAsPrimitive(SessionContext ctx)
    {
        Long value = getUserPK(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getUserPKAsPrimitive()
    {
        return getUserPKAsPrimitive(getSession().getSessionContext());
    }


    protected void setUserPK(SessionContext ctx, Long value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'userPK' is not changeable", 0);
        }
        setProperty(ctx, "userPK", value);
    }


    protected void setUserPK(Long value)
    {
        setUserPK(getSession().getSessionContext(), value);
    }


    protected void setUserPK(SessionContext ctx, long value)
    {
        setUserPK(ctx, Long.valueOf(value));
    }


    protected void setUserPK(long value)
    {
        setUserPK(getSession().getSessionContext(), value);
    }
}
