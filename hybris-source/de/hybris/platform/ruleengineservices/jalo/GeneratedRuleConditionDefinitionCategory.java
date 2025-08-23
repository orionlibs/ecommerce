package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogUnawareMedia;
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
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedRuleConditionDefinitionCategory extends GenericItem
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String ICON = "icon";
    public static final String DEFINITIONS = "definitions";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED = "relation.RuleConditionDefinition2CategoryRelation.source.ordered";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED = "relation.RuleConditionDefinition2CategoryRelation.target.ordered";
    protected static String RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED = "relation.RuleConditionDefinition2CategoryRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("icon", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<RuleConditionDefinition> getDefinitions(SessionContext ctx)
    {
        List<RuleConditionDefinition> items = getLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, "RuleConditionDefinition", null,
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<RuleConditionDefinition> getDefinitions()
    {
        return getDefinitions(getSession().getSessionContext());
    }


    public long getDefinitionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, "RuleConditionDefinition", null);
    }


    public long getDefinitionsCount()
    {
        return getDefinitionsCount(getSession().getSessionContext());
    }


    public void setDefinitions(SessionContext ctx, List<RuleConditionDefinition> value)
    {
        setLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void setDefinitions(List<RuleConditionDefinition> value)
    {
        setDefinitions(getSession().getSessionContext(), value);
    }


    public void addToDefinitions(SessionContext ctx, RuleConditionDefinition value)
    {
        addLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void addToDefinitions(RuleConditionDefinition value)
    {
        addToDefinitions(getSession().getSessionContext(), value);
    }


    public void removeFromDefinitions(SessionContext ctx, RuleConditionDefinition value)
    {
        removeLinkedItems(ctx, false, GeneratedRuleEngineServicesConstants.Relations.RULECONDITIONDEFINITION2CATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(RULECONDITIONDEFINITION2CATEGORYRELATION_MARKMODIFIED));
    }


    public void removeFromDefinitions(RuleConditionDefinition value)
    {
        removeFromDefinitions(getSession().getSessionContext(), value);
    }


    public CatalogUnawareMedia getIcon(SessionContext ctx)
    {
        return (CatalogUnawareMedia)getProperty(ctx, "icon");
    }


    public CatalogUnawareMedia getIcon()
    {
        return getIcon(getSession().getSessionContext());
    }


    public void setIcon(SessionContext ctx, CatalogUnawareMedia value)
    {
        setProperty(ctx, "icon", value);
    }


    public void setIcon(CatalogUnawareMedia value)
    {
        setIcon(getSession().getSessionContext(), value);
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
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("RuleConditionDefinition");
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionCategory.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionCategory.setName requires a session language", 0);
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
}
