package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.jalo.AbstractPromotionAction;
import de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractRuleBasedPromotionAction extends AbstractPromotionAction
{
    public static final String RULE = "rule";
    public static final String STRATEGYID = "strategyId";
    public static final String METADATAHANDLERS = "metadataHandlers";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("rule", Item.AttributeMode.INITIAL);
        tmp.put("strategyId", Item.AttributeMode.INITIAL);
        tmp.put("metadataHandlers", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<String> getMetadataHandlers(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "metadataHandlers");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getMetadataHandlers()
    {
        return getMetadataHandlers(getSession().getSessionContext());
    }


    public void setMetadataHandlers(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "metadataHandlers", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setMetadataHandlers(Collection<String> value)
    {
        setMetadataHandlers(getSession().getSessionContext(), value);
    }


    public AbstractRuleEngineRule getRule(SessionContext ctx)
    {
        return (AbstractRuleEngineRule)getProperty(ctx, "rule");
    }


    public AbstractRuleEngineRule getRule()
    {
        return getRule(getSession().getSessionContext());
    }


    public void setRule(SessionContext ctx, AbstractRuleEngineRule value)
    {
        setProperty(ctx, "rule", value);
    }


    public void setRule(AbstractRuleEngineRule value)
    {
        setRule(getSession().getSessionContext(), value);
    }


    public String getStrategyId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "strategyId");
    }


    public String getStrategyId()
    {
        return getStrategyId(getSession().getSessionContext());
    }


    public void setStrategyId(SessionContext ctx, String value)
    {
        setProperty(ctx, "strategyId", value);
    }


    public void setStrategyId(String value)
    {
        setStrategyId(getSession().getSessionContext(), value);
    }
}
