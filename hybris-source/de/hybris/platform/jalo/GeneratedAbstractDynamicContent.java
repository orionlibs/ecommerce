package de.hybris.platform.jalo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractDynamicContent extends GenericItem
{
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String CHECKSUM = "checksum";
    public static final String CONTENT = "content";
    public static final String VERSION = "version";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("checksum", Item.AttributeMode.INITIAL);
        tmp.put("content", Item.AttributeMode.INITIAL);
        tmp.put("version", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    protected void setActive(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'active' is not changeable", 0);
        }
        setProperty(ctx, "active", value);
    }


    protected void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    protected void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    protected void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public String getChecksum(SessionContext ctx)
    {
        return (String)getProperty(ctx, "checksum");
    }


    public String getChecksum()
    {
        return getChecksum(getSession().getSessionContext());
    }


    public void setChecksum(SessionContext ctx, String value)
    {
        setProperty(ctx, "checksum", value);
    }


    public void setChecksum(String value)
    {
        setChecksum(getSession().getSessionContext(), value);
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


    public String getContent(SessionContext ctx)
    {
        return (String)getProperty(ctx, "content");
    }


    public String getContent()
    {
        return getContent(getSession().getSessionContext());
    }


    public void setContent(SessionContext ctx, String value)
    {
        setProperty(ctx, "content", value);
    }


    public void setContent(String value)
    {
        setContent(getSession().getSessionContext(), value);
    }


    public Long getVersion(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "version");
    }


    public Long getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public long getVersionAsPrimitive(SessionContext ctx)
    {
        Long value = getVersion(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getVersionAsPrimitive()
    {
        return getVersionAsPrimitive(getSession().getSessionContext());
    }


    public void setVersion(SessionContext ctx, Long value)
    {
        setProperty(ctx, "version", value);
    }


    public void setVersion(Long value)
    {
        setVersion(getSession().getSessionContext(), value);
    }


    public void setVersion(SessionContext ctx, long value)
    {
        setVersion(ctx, Long.valueOf(value));
    }


    public void setVersion(long value)
    {
        setVersion(getSession().getSessionContext(), value);
    }
}
