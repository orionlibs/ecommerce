package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ruleengine.constants.GeneratedRuleEngineConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDroolsRule extends AbstractRuleEngineRule
{
    public static final String RULEPACKAGE = "rulePackage";
    public static final String GLOBALS = "globals";
    public static final String KIEBASE = "kieBase";
    protected static final BidirectionalOneToManyHandler<GeneratedDroolsRule> KIEBASEHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSRULE, false, "kieBase", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleEngineRule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("rulePackage", Item.AttributeMode.INITIAL);
        tmp.put("globals", Item.AttributeMode.INITIAL);
        tmp.put("kieBase", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        KIEBASEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Map<String, String> getAllGlobals(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "globals");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllGlobals()
    {
        return getAllGlobals(getSession().getSessionContext());
    }


    public void setAllGlobals(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "globals", value);
    }


    public void setAllGlobals(Map<String, String> value)
    {
        setAllGlobals(getSession().getSessionContext(), value);
    }


    public DroolsKIEBase getKieBase(SessionContext ctx)
    {
        return (DroolsKIEBase)getProperty(ctx, "kieBase");
    }


    public DroolsKIEBase getKieBase()
    {
        return getKieBase(getSession().getSessionContext());
    }


    public void setKieBase(SessionContext ctx, DroolsKIEBase value)
    {
        KIEBASEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setKieBase(DroolsKIEBase value)
    {
        setKieBase(getSession().getSessionContext(), value);
    }


    public String getRulePackage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "rulePackage");
    }


    public String getRulePackage()
    {
        return getRulePackage(getSession().getSessionContext());
    }


    public void setRulePackage(SessionContext ctx, String value)
    {
        setProperty(ctx, "rulePackage", value);
    }


    public void setRulePackage(String value)
    {
        setRulePackage(getSession().getSessionContext(), value);
    }
}
