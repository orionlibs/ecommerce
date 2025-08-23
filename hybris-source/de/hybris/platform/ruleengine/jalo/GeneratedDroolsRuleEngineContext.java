package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDroolsRuleEngineContext extends AbstractRuleEngineContext
{
    public static final String KIESESSION = "kieSession";
    public static final String RULEFIRINGLIMIT = "ruleFiringLimit";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleEngineContext.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("kieSession", Item.AttributeMode.INITIAL);
        tmp.put("ruleFiringLimit", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public DroolsKIESession getKieSession(SessionContext ctx)
    {
        return (DroolsKIESession)getProperty(ctx, "kieSession");
    }


    public DroolsKIESession getKieSession()
    {
        return getKieSession(getSession().getSessionContext());
    }


    public void setKieSession(SessionContext ctx, DroolsKIESession value)
    {
        setProperty(ctx, "kieSession", value);
    }


    public void setKieSession(DroolsKIESession value)
    {
        setKieSession(getSession().getSessionContext(), value);
    }


    public Long getRuleFiringLimit(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "ruleFiringLimit");
    }


    public Long getRuleFiringLimit()
    {
        return getRuleFiringLimit(getSession().getSessionContext());
    }


    public long getRuleFiringLimitAsPrimitive(SessionContext ctx)
    {
        Long value = getRuleFiringLimit(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getRuleFiringLimitAsPrimitive()
    {
        return getRuleFiringLimitAsPrimitive(getSession().getSessionContext());
    }


    public void setRuleFiringLimit(SessionContext ctx, Long value)
    {
        setProperty(ctx, "ruleFiringLimit", value);
    }


    public void setRuleFiringLimit(Long value)
    {
        setRuleFiringLimit(getSession().getSessionContext(), value);
    }


    public void setRuleFiringLimit(SessionContext ctx, long value)
    {
        setRuleFiringLimit(ctx, Long.valueOf(value));
    }


    public void setRuleFiringLimit(long value)
    {
        setRuleFiringLimit(getSession().getSessionContext(), value);
    }
}
