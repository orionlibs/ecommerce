package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedRuleActionDefinition extends GenericItem
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String BREADCRUMB = "breadcrumb";
    public static final String TRANSLATORID = "translatorId";
    public static final String TRANSLATORPARAMETERS = "translatorParameters";
    public static final String CATEGORIES = "categories";
    protected static String RULEACTIONDEFINITION2CATEGORYRELATION_SRC_ORDERED = "relation.RuleActionDefinition2CategoryRelation.source.ordered";
    protected static String RULEACTIONDEFINITION2CATEGORYRELATION_TGT_ORDERED = "relation.RuleActionDefinition2CategoryRelation.target.ordered";
    protected static String RULEACTIONDEFINITION2CATEGORYRELATION_MARKMODIFIED = "relation.RuleActionDefinition2CategoryRelation.markmodified";
    public static final String PARAMETERS = "parameters";
    public static final String RULETYPES = "ruleTypes";
    protected static final OneToManyHandler<RuleActionDefinitionParameter> PARAMETERSHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONPARAMETER, true, "definition", "definitionPOS", true, true, 2);
    protected static final OneToManyHandler<RuleActionDefinitionRuleTypeMapping> RULETYPESHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULEACTIONDEFINITIONRULETYPEMAPPING, true, "definition", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("breadcrumb", Item.AttributeMode.INITIAL);
        tmp.put("translatorId", Item.AttributeMode.INITIAL);
        tmp.put("translatorParameters", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getBreadcrumb(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleActionDefinition.getBreadcrumb requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "breadcrumb");
    }


    public String getBreadcrumb()
    {
        return getBreadcrumb(getSession().getSessionContext());
    }


    public Map<Language, String> getAllBreadcrumb(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "breadcrumb", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllBreadcrumb()
    {
        return getAllBreadcrumb(getSession().getSessionContext());
    }


    public void setBreadcrumb(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleActionDefinition.setBreadcrumb requires a session language", 0);
        }
        setLocalizedProperty(ctx, "breadcrumb", value);
    }


    public void setBreadcrumb(String value)
    {
        setBreadcrumb(getSession().getSessionContext(), value);
    }


    public void setAllBreadcrumb(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "breadcrumb", value);
    }


    public void setAllBreadcrumb(Map<Language, String> value)
    {
        setAllBreadcrumb(getSession().getSessionContext(), value);
    }


    public List<RuleActionDefinitionCategory> getCategories(SessionContext ctx)
    {
        List<RuleActionDefinitionCategory> items = getLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULEACTIONDEFINITION2CATEGORYRELATION, "RuleActionDefinitionCategory", null,
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<RuleActionDefinitionCategory> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULEACTIONDEFINITION2CATEGORYRELATION, "RuleActionDefinitionCategory", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, List<RuleActionDefinitionCategory> value)
    {
        setLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULEACTIONDEFINITION2CATEGORYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULEACTIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void setCategories(List<RuleActionDefinitionCategory> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, RuleActionDefinitionCategory value)
    {
        addLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULEACTIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULEACTIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void addToCategories(RuleActionDefinitionCategory value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, RuleActionDefinitionCategory value)
    {
        removeLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULEACTIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULEACTIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULEACTIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void removeFromCategories(RuleActionDefinitionCategory value)
    {
        removeFromCategories(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("RuleActionDefinitionCategory");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RULEACTIONDEFINITION2CATEGORYRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleActionDefinition.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleActionDefinition.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public List<RuleActionDefinitionParameter> getParameters(SessionContext ctx)
    {
        return (List<RuleActionDefinitionParameter>)PARAMETERSHANDLER.getValues(ctx, (Item)this);
    }


    public List<RuleActionDefinitionParameter> getParameters()
    {
        return getParameters(getSession().getSessionContext());
    }


    public void setParameters(SessionContext ctx, List<RuleActionDefinitionParameter> value)
    {
        PARAMETERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setParameters(List<RuleActionDefinitionParameter> value)
    {
        setParameters(getSession().getSessionContext(), value);
    }


    public void addToParameters(SessionContext ctx, RuleActionDefinitionParameter value)
    {
        PARAMETERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToParameters(RuleActionDefinitionParameter value)
    {
        addToParameters(getSession().getSessionContext(), value);
    }


    public void removeFromParameters(SessionContext ctx, RuleActionDefinitionParameter value)
    {
        PARAMETERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromParameters(RuleActionDefinitionParameter value)
    {
        removeFromParameters(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public List<RuleActionDefinitionRuleTypeMapping> getRuleTypes(SessionContext ctx)
    {
        return (List<RuleActionDefinitionRuleTypeMapping>)RULETYPESHANDLER.getValues(ctx, (Item)this);
    }


    public List<RuleActionDefinitionRuleTypeMapping> getRuleTypes()
    {
        return getRuleTypes(getSession().getSessionContext());
    }


    public void setRuleTypes(SessionContext ctx, List<RuleActionDefinitionRuleTypeMapping> value)
    {
        RULETYPESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRuleTypes(List<RuleActionDefinitionRuleTypeMapping> value)
    {
        setRuleTypes(getSession().getSessionContext(), value);
    }


    public void addToRuleTypes(SessionContext ctx, RuleActionDefinitionRuleTypeMapping value)
    {
        RULETYPESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRuleTypes(RuleActionDefinitionRuleTypeMapping value)
    {
        addToRuleTypes(getSession().getSessionContext(), value);
    }


    public void removeFromRuleTypes(SessionContext ctx, RuleActionDefinitionRuleTypeMapping value)
    {
        RULETYPESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRuleTypes(RuleActionDefinitionRuleTypeMapping value)
    {
        removeFromRuleTypes(getSession().getSessionContext(), value);
    }


    public String getTranslatorId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "translatorId");
    }


    public String getTranslatorId()
    {
        return getTranslatorId(getSession().getSessionContext());
    }


    public void setTranslatorId(SessionContext ctx, String value)
    {
        setProperty(ctx, "translatorId", value);
    }


    public void setTranslatorId(String value)
    {
        setTranslatorId(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllTranslatorParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "translatorParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllTranslatorParameters()
    {
        return getAllTranslatorParameters(getSession().getSessionContext());
    }


    public void setAllTranslatorParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "translatorParameters", value);
    }


    public void setAllTranslatorParameters(Map<String, String> value)
    {
        setAllTranslatorParameters(getSession().getSessionContext(), value);
    }
}
