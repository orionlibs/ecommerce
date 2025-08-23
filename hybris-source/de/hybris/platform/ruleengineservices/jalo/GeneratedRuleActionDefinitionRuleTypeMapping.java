package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleActionDefinitionRuleTypeMapping extends GenericItem
{
    public static final String RULETYPE = "ruleType";
    public static final String DEFINITION = "definition";
    protected static final BidirectionalOneToManyHandler<GeneratedRuleActionDefinitionRuleTypeMapping> DEFINITIONHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONRULETYPEMAPPING, false, "definition", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("ruleType", Item.AttributeMode.INITIAL);
        tmp.put("definition", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        DEFINITIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public RuleActionDefinition getDefinition(SessionContext ctx)
    {
        return (RuleActionDefinition)getProperty(ctx, "definition");
    }


    public RuleActionDefinition getDefinition()
    {
        return getDefinition(getSession().getSessionContext());
    }


    public void setDefinition(SessionContext ctx, RuleActionDefinition value)
    {
        DEFINITIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setDefinition(RuleActionDefinition value)
    {
        setDefinition(getSession().getSessionContext(), value);
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
