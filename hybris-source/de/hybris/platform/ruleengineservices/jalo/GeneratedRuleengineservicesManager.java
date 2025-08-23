package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.validation.jalo.constraints.jsr303.ObjectPatternConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedRuleengineservicesManager extends Extension
{
    protected static String CAMPAIGN2SOURCERULERELATION_SRC_ORDERED = "relation.Campaign2SourceRuleRelation.source.ordered";
    protected static String CAMPAIGN2SOURCERULERELATION_TGT_ORDERED = "relation.Campaign2SourceRuleRelation.target.ordered";
    protected static String CAMPAIGN2SOURCERULERELATION_MARKMODIFIED = "relation.Campaign2SourceRuleRelation.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("ruleParameters", Item.AttributeMode.INITIAL);
        tmp.put("maxAllowedRuns", Item.AttributeMode.INITIAL);
        tmp.put("ruleGroupCode", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        tmp.put("sourceRule", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public ObjectPatternConstraint createObjectPatternConstraint(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.OBJECTPATTERNCONSTRAINT);
            return (ObjectPatternConstraint)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ObjectPatternConstraint : " + e.getMessage(), 0);
        }
    }


    public ObjectPatternConstraint createObjectPatternConstraint(Map attributeValues)
    {
        return createObjectPatternConstraint(getSession().getSessionContext(), attributeValues);
    }


    public RuleActionDefinition createRuleActionDefinition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITION);
            return (RuleActionDefinition)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleActionDefinition : " + e.getMessage(), 0);
        }
    }


    public RuleActionDefinition createRuleActionDefinition(Map attributeValues)
    {
        return createRuleActionDefinition(getSession().getSessionContext(), attributeValues);
    }


    public RuleActionDefinitionCategory createRuleActionDefinitionCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONCATEGORY);
            return (RuleActionDefinitionCategory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleActionDefinitionCategory : " + e.getMessage(), 0);
        }
    }


    public RuleActionDefinitionCategory createRuleActionDefinitionCategory(Map attributeValues)
    {
        return createRuleActionDefinitionCategory(getSession().getSessionContext(), attributeValues);
    }


    public RuleActionDefinitionParameter createRuleActionDefinitionParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONPARAMETER);
            return (RuleActionDefinitionParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleActionDefinitionParameter : " + e.getMessage(), 0);
        }
    }


    public RuleActionDefinitionParameter createRuleActionDefinitionParameter(Map attributeValues)
    {
        return createRuleActionDefinitionParameter(getSession().getSessionContext(), attributeValues);
    }


    public RuleActionDefinitionRuleTypeMapping createRuleActionDefinitionRuleTypeMapping(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONRULETYPEMAPPING);
            return (RuleActionDefinitionRuleTypeMapping)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleActionDefinitionRuleTypeMapping : " + e.getMessage(), 0);
        }
    }


    public RuleActionDefinitionRuleTypeMapping createRuleActionDefinitionRuleTypeMapping(Map attributeValues)
    {
        return createRuleActionDefinitionRuleTypeMapping(getSession().getSessionContext(), attributeValues);
    }


    public RuleConditionDefinition createRuleConditionDefinition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITION);
            return (RuleConditionDefinition)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleConditionDefinition : " + e.getMessage(), 0);
        }
    }


    public RuleConditionDefinition createRuleConditionDefinition(Map attributeValues)
    {
        return createRuleConditionDefinition(getSession().getSessionContext(), attributeValues);
    }


    public RuleConditionDefinitionCategory createRuleConditionDefinitionCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONCATEGORY);
            return (RuleConditionDefinitionCategory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleConditionDefinitionCategory : " + e.getMessage(), 0);
        }
    }


    public RuleConditionDefinitionCategory createRuleConditionDefinitionCategory(Map attributeValues)
    {
        return createRuleConditionDefinitionCategory(getSession().getSessionContext(), attributeValues);
    }


    public RuleConditionDefinitionParameter createRuleConditionDefinitionParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONPARAMETER);
            return (RuleConditionDefinitionParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleConditionDefinitionParameter : " + e.getMessage(), 0);
        }
    }


    public RuleConditionDefinitionParameter createRuleConditionDefinitionParameter(Map attributeValues)
    {
        return createRuleConditionDefinitionParameter(getSession().getSessionContext(), attributeValues);
    }


    public RuleConditionDefinitionRuleTypeMapping createRuleConditionDefinitionRuleTypeMapping(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONRULETYPEMAPPING);
            return (RuleConditionDefinitionRuleTypeMapping)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleConditionDefinitionRuleTypeMapping : " + e.getMessage(), 0);
        }
    }


    public RuleConditionDefinitionRuleTypeMapping createRuleConditionDefinitionRuleTypeMapping(Map attributeValues)
    {
        return createRuleConditionDefinitionRuleTypeMapping(getSession().getSessionContext(), attributeValues);
    }


    public RuleEngineCronJob createRuleEngineCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEENGINECRONJOB);
            return (RuleEngineCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleEngineCronJob : " + e.getMessage(), 0);
        }
    }


    public RuleEngineCronJob createRuleEngineCronJob(Map attributeValues)
    {
        return createRuleEngineCronJob(getSession().getSessionContext(), attributeValues);
    }


    public RuleEngineJob createRuleEngineJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEENGINEJOB);
            return (RuleEngineJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleEngineJob : " + e.getMessage(), 0);
        }
    }


    public RuleEngineJob createRuleEngineJob(Map attributeValues)
    {
        return createRuleEngineJob(getSession().getSessionContext(), attributeValues);
    }


    public RuleGroup createRuleGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULEGROUP);
            return (RuleGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleGroup : " + e.getMessage(), 0);
        }
    }


    public RuleGroup createRuleGroup(Map attributeValues)
    {
        return createRuleGroup(getSession().getSessionContext(), attributeValues);
    }


    public RuleToEngineRuleTypeMapping createRuleToEngineRuleTypeMapping(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.RULETOENGINERULETYPEMAPPING);
            return (RuleToEngineRuleTypeMapping)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RuleToEngineRuleTypeMapping : " + e.getMessage(), 0);
        }
    }


    public RuleToEngineRuleTypeMapping createRuleToEngineRuleTypeMapping(Map attributeValues)
    {
        return createRuleToEngineRuleTypeMapping(getSession().getSessionContext(), attributeValues);
    }


    public SourceRule createSourceRule(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.SOURCERULE);
            return (SourceRule)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SourceRule : " + e.getMessage(), 0);
        }
    }


    public SourceRule createSourceRule(Map attributeValues)
    {
        return createSourceRule(getSession().getSessionContext(), attributeValues);
    }


    public SourceRuleTemplate createSourceRuleTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedRuleEngineServicesConstants.TC.SOURCERULETEMPLATE);
            return (SourceRuleTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SourceRuleTemplate : " + e.getMessage(), 0);
        }
    }


    public SourceRuleTemplate createSourceRuleTemplate(Map attributeValues)
    {
        return createSourceRuleTemplate(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "ruleengineservices";
    }


    public Integer getMaxAllowedRuns(SessionContext ctx, AbstractRuleEngineRule item)
    {
        return (Integer)item.getProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MAXALLOWEDRUNS);
    }


    public Integer getMaxAllowedRuns(AbstractRuleEngineRule item)
    {
        return getMaxAllowedRuns(getSession().getSessionContext(), item);
    }


    public int getMaxAllowedRunsAsPrimitive(SessionContext ctx, AbstractRuleEngineRule item)
    {
        Integer value = getMaxAllowedRuns(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxAllowedRunsAsPrimitive(AbstractRuleEngineRule item)
    {
        return getMaxAllowedRunsAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setMaxAllowedRuns(SessionContext ctx, AbstractRuleEngineRule item, Integer value)
    {
        item.setProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MAXALLOWEDRUNS, value);
    }


    public void setMaxAllowedRuns(AbstractRuleEngineRule item, Integer value)
    {
        setMaxAllowedRuns(getSession().getSessionContext(), item, value);
    }


    public void setMaxAllowedRuns(SessionContext ctx, AbstractRuleEngineRule item, int value)
    {
        setMaxAllowedRuns(ctx, item, Integer.valueOf(value));
    }


    public void setMaxAllowedRuns(AbstractRuleEngineRule item, int value)
    {
        setMaxAllowedRuns(getSession().getSessionContext(), item, value);
    }


    public String getMessageFired(SessionContext ctx, AbstractRuleEngineRule item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRuleEngineRule.getMessageFired requires a session language", 0);
        }
        return (String)item.getLocalizedProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFIRED);
    }


    public String getMessageFired(AbstractRuleEngineRule item)
    {
        return getMessageFired(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllMessageFired(SessionContext ctx, AbstractRuleEngineRule item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFIRED, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageFired(AbstractRuleEngineRule item)
    {
        return getAllMessageFired(getSession().getSessionContext(), item);
    }


    public void setMessageFired(SessionContext ctx, AbstractRuleEngineRule item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRuleEngineRule.setMessageFired requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFIRED, value);
    }


    public void setMessageFired(AbstractRuleEngineRule item, String value)
    {
        setMessageFired(getSession().getSessionContext(), item, value);
    }


    public void setAllMessageFired(SessionContext ctx, AbstractRuleEngineRule item, Map<Language, String> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.MESSAGEFIRED, value);
    }


    public void setAllMessageFired(AbstractRuleEngineRule item, Map<Language, String> value)
    {
        setAllMessageFired(getSession().getSessionContext(), item, value);
    }


    public String getRuleGroupCode(SessionContext ctx, AbstractRuleEngineRule item)
    {
        return (String)item.getProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.RULEGROUPCODE);
    }


    public String getRuleGroupCode(AbstractRuleEngineRule item)
    {
        return getRuleGroupCode(getSession().getSessionContext(), item);
    }


    public void setRuleGroupCode(SessionContext ctx, AbstractRuleEngineRule item, String value)
    {
        item.setProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.RULEGROUPCODE, value);
    }


    public void setRuleGroupCode(AbstractRuleEngineRule item, String value)
    {
        setRuleGroupCode(getSession().getSessionContext(), item, value);
    }


    public String getRuleParameters(SessionContext ctx, AbstractRuleEngineRule item)
    {
        return (String)item.getProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.RULEPARAMETERS);
    }


    public String getRuleParameters(AbstractRuleEngineRule item)
    {
        return getRuleParameters(getSession().getSessionContext(), item);
    }


    public void setRuleParameters(SessionContext ctx, AbstractRuleEngineRule item, String value)
    {
        item.setProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.RULEPARAMETERS, value);
    }


    public void setRuleParameters(AbstractRuleEngineRule item, String value)
    {
        setRuleParameters(getSession().getSessionContext(), item, value);
    }


    public AbstractRule getSourceRule(SessionContext ctx, AbstractRuleEngineRule item)
    {
        return (AbstractRule)item.getProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.SOURCERULE);
    }


    public AbstractRule getSourceRule(AbstractRuleEngineRule item)
    {
        return getSourceRule(getSession().getSessionContext(), item);
    }


    public void setSourceRule(SessionContext ctx, AbstractRuleEngineRule item, AbstractRule value)
    {
        item.setProperty(ctx, GeneratedRuleEngineServicesConstants.Attributes.AbstractRuleEngineRule.SOURCERULE, value);
    }


    public void setSourceRule(AbstractRuleEngineRule item, AbstractRule value)
    {
        setSourceRule(getSession().getSessionContext(), item, value);
    }


    public Set<SourceRule> getSourceRules(SessionContext ctx, Campaign item)
    {
        List<SourceRule> items = item.getLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, "SourceRule", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<SourceRule> getSourceRules(Campaign item)
    {
        return getSourceRules(getSession().getSessionContext(), item);
    }


    public long getSourceRulesCount(SessionContext ctx, Campaign item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, "SourceRule", null);
    }


    public long getSourceRulesCount(Campaign item)
    {
        return getSourceRulesCount(getSession().getSessionContext(), item);
    }


    public void setSourceRules(SessionContext ctx, Campaign item, Set<SourceRule> value)
    {
        item.setLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void setSourceRules(Campaign item, Set<SourceRule> value)
    {
        setSourceRules(getSession().getSessionContext(), item, value);
    }


    public void addToSourceRules(SessionContext ctx, Campaign item, SourceRule value)
    {
        item.addLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void addToSourceRules(Campaign item, SourceRule value)
    {
        addToSourceRules(getSession().getSessionContext(), item, value);
    }


    public void removeFromSourceRules(SessionContext ctx, Campaign item, SourceRule value)
    {
        item.removeLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.CAMPAIGN2SOURCERULERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGN2SOURCERULERELATION_MARKMODIFIED));
    }


    public void removeFromSourceRules(Campaign item, SourceRule value)
    {
        removeFromSourceRules(getSession().getSessionContext(), item, value);
    }
}
