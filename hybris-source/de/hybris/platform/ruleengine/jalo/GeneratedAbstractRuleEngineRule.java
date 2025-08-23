package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractRuleEngineRule extends GenericItem
{
    public static final String UUID = "uuid";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String RULECONTENT = "ruleContent";
    public static final String RULETYPE = "ruleType";
    public static final String CHECKSUM = "checksum";
    public static final String CURRENTVERSION = "currentVersion";
    public static final String VERSION = "version";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uuid", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("ruleContent", Item.AttributeMode.INITIAL);
        tmp.put("ruleType", Item.AttributeMode.INITIAL);
        tmp.put("checksum", Item.AttributeMode.INITIAL);
        tmp.put("currentVersion", Item.AttributeMode.INITIAL);
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


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
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


    public Boolean isCurrentVersion(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "currentVersion");
    }


    public Boolean isCurrentVersion()
    {
        return isCurrentVersion(getSession().getSessionContext());
    }


    public boolean isCurrentVersionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCurrentVersion(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCurrentVersionAsPrimitive()
    {
        return isCurrentVersionAsPrimitive(getSession().getSessionContext());
    }


    public void setCurrentVersion(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "currentVersion", value);
    }


    public void setCurrentVersion(Boolean value)
    {
        setCurrentVersion(getSession().getSessionContext(), value);
    }


    public void setCurrentVersion(SessionContext ctx, boolean value)
    {
        setCurrentVersion(ctx, Boolean.valueOf(value));
    }


    public void setCurrentVersion(boolean value)
    {
        setCurrentVersion(getSession().getSessionContext(), value);
    }


    public String getRuleContent(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ruleContent");
    }


    public String getRuleContent()
    {
        return getRuleContent(getSession().getSessionContext());
    }


    public void setRuleContent(SessionContext ctx, String value)
    {
        setProperty(ctx, "ruleContent", value);
    }


    public void setRuleContent(String value)
    {
        setRuleContent(getSession().getSessionContext(), value);
    }


    public EnumerationValue getRuleType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "ruleType");
    }


    public EnumerationValue getRuleType()
    {
        return getRuleType(getSession().getSessionContext());
    }


    public void setRuleType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "ruleType", value);
    }


    public void setRuleType(EnumerationValue value)
    {
        setRuleType(getSession().getSessionContext(), value);
    }


    public String getUuid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uuid");
    }


    public String getUuid()
    {
        return getUuid(getSession().getSessionContext());
    }


    public void setUuid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uuid", value);
    }


    public void setUuid(String value)
    {
        setUuid(getSession().getSessionContext(), value);
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
