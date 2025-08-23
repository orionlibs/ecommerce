package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractRulesModule extends GenericItem
{
    public static final String NAME = "name";
    public static final String RULETYPE = "ruleType";
    public static final String ACTIVE = "active";
    public static final String VERSION = "version";
    public static final String ALLOWEDTARGETS = "allowedTargets";
    public static final String LOCKACQUIRED = "lockAcquired";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("ruleType", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("allowedTargets", Item.AttributeMode.INITIAL);
        tmp.put("lockAcquired", Item.AttributeMode.INITIAL);
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


    public List<AbstractRulesModule> getAllowedTargets(SessionContext ctx)
    {
        List<AbstractRulesModule> coll = (List<AbstractRulesModule>)getProperty(ctx, "allowedTargets");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<AbstractRulesModule> getAllowedTargets()
    {
        return getAllowedTargets(getSession().getSessionContext());
    }


    public void setAllowedTargets(SessionContext ctx, List<AbstractRulesModule> value)
    {
        setProperty(ctx, "allowedTargets", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAllowedTargets(List<AbstractRulesModule> value)
    {
        setAllowedTargets(getSession().getSessionContext(), value);
    }


    public Boolean isLockAcquired(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "lockAcquired");
    }


    public Boolean isLockAcquired()
    {
        return isLockAcquired(getSession().getSessionContext());
    }


    public boolean isLockAcquiredAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLockAcquired(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLockAcquiredAsPrimitive()
    {
        return isLockAcquiredAsPrimitive(getSession().getSessionContext());
    }


    public void setLockAcquired(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "lockAcquired", value);
    }


    public void setLockAcquired(Boolean value)
    {
        setLockAcquired(getSession().getSessionContext(), value);
    }


    public void setLockAcquired(SessionContext ctx, boolean value)
    {
        setLockAcquired(ctx, Boolean.valueOf(value));
    }


    public void setLockAcquired(boolean value)
    {
        setLockAcquired(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
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
