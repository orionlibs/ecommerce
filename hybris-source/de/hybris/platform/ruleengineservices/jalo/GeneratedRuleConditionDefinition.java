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

public abstract class GeneratedRuleConditionDefinition extends GenericItem
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String BREADCRUMB = "breadcrumb";
    public static final String ALLOWSCHILDREN = "allowsChildren";
    public static final String TRANSLATORID = "translatorId";
    public static final String TRANSLATORPARAMETERS = "translatorParameters";
    public static final String CATEGORIES = "categories";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED = "relation.RuleConditionDefinition2CategoryRelation.source.ordered";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED = "relation.RuleConditionDefinition2CategoryRelation.target.ordered";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED = "relation.RuleConditionDefinition2CategoryRelation.markmodified";
    public static final String PARAMETERS = "parameters";
    public static final String RULETYPES = "ruleTypes";
    protected static final OneToManyHandler<RuleConditionDefinitionParameter> PARAMETERSHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONPARAMETER, true, "definition", "definitionPOS", true, true, 2);
    protected static final OneToManyHandler<RuleConditionDefinitionRuleTypeMapping> RULETYPESHANDLER = new OneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONRULETYPEMAPPING, true, "definition", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("breadcrumb", Item.AttributeMode.INITIAL);
        tmp.put("allowsChildren", Item.AttributeMode.INITIAL);
        tmp.put("translatorId", Item.AttributeMode.INITIAL);
        tmp.put("translatorParameters", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllowsChildren(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allowsChildren");
    }


    public Boolean isAllowsChildren()
    {
        return isAllowsChildren(getSession().getSessionContext());
    }


    public boolean isAllowsChildrenAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllowsChildren(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllowsChildrenAsPrimitive()
    {
        return isAllowsChildrenAsPrimitive(getSession().getSessionContext());
    }


    public void setAllowsChildren(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allowsChildren", value);
    }


    public void setAllowsChildren(Boolean value)
    {
        setAllowsChildren(getSession().getSessionContext(), value);
    }


    public void setAllowsChildren(SessionContext ctx, boolean value)
    {
        setAllowsChildren(ctx, Boolean.valueOf(value));
    }


    public void setAllowsChildren(boolean value)
    {
        setAllowsChildren(getSession().getSessionContext(), value);
    }


    public String getBreadcrumb(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinition.getBreadcrumb requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinition.setBreadcrumb requires a session language", 0);
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


    public List<RuleConditionDefinitionCategory> getCategories(SessionContext ctx)
    {
        List<RuleConditionDefinitionCategory> items = getLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, "RuleConditionDefinitionCategory", null,
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<RuleConditionDefinitionCategory> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, "RuleConditionDefinitionCategory", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, List<RuleConditionDefinitionCategory> value)
    {
        setLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void setCategories(List<RuleConditionDefinitionCategory> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, RuleConditionDefinitionCategory value)
    {
        addLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void addToCategories(RuleConditionDefinitionCategory value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, RuleConditionDefinitionCategory value)
    {
        removeLinkedItems(ctx, true, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void removeFromCategories(RuleConditionDefinitionCategory value)
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
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("RuleConditionDefinitionCategory");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinition.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinition.setName requires a session language", 0);
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


    public List<RuleConditionDefinitionParameter> getParameters(SessionContext ctx)
    {
        return (List<RuleConditionDefinitionParameter>)PARAMETERSHANDLER.getValues(ctx, (Item)this);
    }


    public List<RuleConditionDefinitionParameter> getParameters()
    {
        return getParameters(getSession().getSessionContext());
    }


    public void setParameters(SessionContext ctx, List<RuleConditionDefinitionParameter> value)
    {
        PARAMETERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setParameters(List<RuleConditionDefinitionParameter> value)
    {
        setParameters(getSession().getSessionContext(), value);
    }


    public void addToParameters(SessionContext ctx, RuleConditionDefinitionParameter value)
    {
        PARAMETERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToParameters(RuleConditionDefinitionParameter value)
    {
        addToParameters(getSession().getSessionContext(), value);
    }


    public void removeFromParameters(SessionContext ctx, RuleConditionDefinitionParameter value)
    {
        PARAMETERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromParameters(RuleConditionDefinitionParameter value)
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


    public List<RuleConditionDefinitionRuleTypeMapping> getRuleTypes(SessionContext ctx)
    {
        return (List<RuleConditionDefinitionRuleTypeMapping>)RULETYPESHANDLER.getValues(ctx, (Item)this);
    }


    public List<RuleConditionDefinitionRuleTypeMapping> getRuleTypes()
    {
        return getRuleTypes(getSession().getSessionContext());
    }


    public void setRuleTypes(SessionContext ctx, List<RuleConditionDefinitionRuleTypeMapping> value)
    {
        RULETYPESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRuleTypes(List<RuleConditionDefinitionRuleTypeMapping> value)
    {
        setRuleTypes(getSession().getSessionContext(), value);
    }


    public void addToRuleTypes(SessionContext ctx, RuleConditionDefinitionRuleTypeMapping value)
    {
        RULETYPESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRuleTypes(RuleConditionDefinitionRuleTypeMapping value)
    {
        addToRuleTypes(getSession().getSessionContext(), value);
    }


    public void removeFromRuleTypes(SessionContext ctx, RuleConditionDefinitionRuleTypeMapping value)
    {
        RULETYPESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRuleTypes(RuleConditionDefinitionRuleTypeMapping value)
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
