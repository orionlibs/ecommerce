package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedRuleGroup extends GenericItem
{
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String EXCLUSIVE = "exclusive";
    public static final String RULES = "rules";
    public static final String RULETEMPLATES = "ruleTemplates";
    protected static final OneToManyHandler<AbstractRule> RULESHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.ABSTRACTRULE, false, "ruleGroup", null, false, true, 1);
    protected static final OneToManyHandler<AbstractRuleTemplate> RULETEMPLATESHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.ABSTRACTRULETEMPLATE, false, "ruleGroup", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("exclusive", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleGroup.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleGroup.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Boolean isExclusive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "exclusive");
    }


    public Boolean isExclusive()
    {
        return isExclusive(getSession().getSessionContext());
    }


    public boolean isExclusiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExclusive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExclusiveAsPrimitive()
    {
        return isExclusiveAsPrimitive(getSession().getSessionContext());
    }


    public void setExclusive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "exclusive", value);
    }


    public void setExclusive(Boolean value)
    {
        setExclusive(getSession().getSessionContext(), value);
    }


    public void setExclusive(SessionContext ctx, boolean value)
    {
        setExclusive(ctx, Boolean.valueOf(value));
    }


    public void setExclusive(boolean value)
    {
        setExclusive(getSession().getSessionContext(), value);
    }


    public Set<AbstractRule> getRules(SessionContext ctx)
    {
        return (Set<AbstractRule>)RULESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<AbstractRule> getRules()
    {
        return getRules(getSession().getSessionContext());
    }


    public void setRules(SessionContext ctx, Set<AbstractRule> value)
    {
        RULESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRules(Set<AbstractRule> value)
    {
        setRules(getSession().getSessionContext(), value);
    }


    public void addToRules(SessionContext ctx, AbstractRule value)
    {
        RULESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRules(AbstractRule value)
    {
        addToRules(getSession().getSessionContext(), value);
    }


    public void removeFromRules(SessionContext ctx, AbstractRule value)
    {
        RULESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRules(AbstractRule value)
    {
        removeFromRules(getSession().getSessionContext(), value);
    }


    public Set<AbstractRuleTemplate> getRuleTemplates(SessionContext ctx)
    {
        return (Set<AbstractRuleTemplate>)RULETEMPLATESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<AbstractRuleTemplate> getRuleTemplates()
    {
        return getRuleTemplates(getSession().getSessionContext());
    }


    public void setRuleTemplates(SessionContext ctx, Set<AbstractRuleTemplate> value)
    {
        RULETEMPLATESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRuleTemplates(Set<AbstractRuleTemplate> value)
    {
        setRuleTemplates(getSession().getSessionContext(), value);
    }


    public void addToRuleTemplates(SessionContext ctx, AbstractRuleTemplate value)
    {
        RULETEMPLATESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRuleTemplates(AbstractRuleTemplate value)
    {
        addToRuleTemplates(getSession().getSessionContext(), value);
    }


    public void removeFromRuleTemplates(SessionContext ctx, AbstractRuleTemplate value)
    {
        RULETEMPLATESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRuleTemplates(AbstractRuleTemplate value)
    {
        removeFromRuleTemplates(getSession().getSessionContext(), value);
    }
}
