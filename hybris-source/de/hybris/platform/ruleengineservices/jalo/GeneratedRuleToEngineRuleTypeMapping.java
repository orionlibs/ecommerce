package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleToEngineRuleTypeMapping extends GenericItem
{
    public static final String RULETYPE = "ruleType";
    public static final String ENGINERULETYPE = "engineRuleType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("ruleType", Item.AttributeMode.INITIAL);
        tmp.put("engineRuleType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getEngineRuleType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "engineRuleType");
    }


    public EnumerationValue getEngineRuleType()
    {
        return getEngineRuleType(getSession().getSessionContext());
    }


    public void setEngineRuleType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "engineRuleType", value);
    }


    public void setEngineRuleType(EnumerationValue value)
    {
        setEngineRuleType(getSession().getSessionContext(), value);
    }


    public ComposedType getRuleType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "ruleType");
    }


    public ComposedType getRuleType()
    {
        return getRuleType(getSession().getSessionContext());
    }


    public void setRuleType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "ruleType", value);
    }


    public void setRuleType(ComposedType value)
    {
        setRuleType(getSession().getSessionContext(), value);
    }
}
