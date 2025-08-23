package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedRuleConditionDefinitionParameter extends GenericItem
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String REQUIRED = "required";
    public static final String VALIDATORS = "validators";
    public static final String FILTERS = "filters";
    public static final String DEFAULTEDITOR = "defaultEditor";
    public static final String DEFINITIONPOS = "definitionPOS";
    public static final String DEFINITION = "definition";
    protected static final BidirectionalOneToManyHandler<GeneratedRuleConditionDefinitionParameter> DEFINITIONHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineServicesConstants.TC.RULECONDITIONDEFINITIONPARAMETER, false, "definition", "definitionPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("required", Item.AttributeMode.INITIAL);
        tmp.put("validators", Item.AttributeMode.INITIAL);
        tmp.put("filters", Item.AttributeMode.INITIAL);
        tmp.put("defaultEditor", Item.AttributeMode.INITIAL);
        tmp.put("definitionPOS", Item.AttributeMode.INITIAL);
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


    public String getDefaultEditor(SessionContext ctx)
    {
        return (String)getProperty(ctx, "defaultEditor");
    }


    public String getDefaultEditor()
    {
        return getDefaultEditor(getSession().getSessionContext());
    }


    public void setDefaultEditor(SessionContext ctx, String value)
    {
        setProperty(ctx, "defaultEditor", value);
    }


    public void setDefaultEditor(String value)
    {
        setDefaultEditor(getSession().getSessionContext(), value);
    }


    public RuleConditionDefinition getDefinition(SessionContext ctx)
    {
        return (RuleConditionDefinition)getProperty(ctx, "definition");
    }


    public RuleConditionDefinition getDefinition()
    {
        return getDefinition(getSession().getSessionContext());
    }


    public void setDefinition(SessionContext ctx, RuleConditionDefinition value)
    {
        DEFINITIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setDefinition(RuleConditionDefinition value)
    {
        setDefinition(getSession().getSessionContext(), value);
    }


    Integer getDefinitionPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "definitionPOS");
    }


    Integer getDefinitionPOS()
    {
        return getDefinitionPOS(getSession().getSessionContext());
    }


    int getDefinitionPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getDefinitionPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getDefinitionPOSAsPrimitive()
    {
        return getDefinitionPOSAsPrimitive(getSession().getSessionContext());
    }


    void setDefinitionPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "definitionPOS", value);
    }


    void setDefinitionPOS(Integer value)
    {
        setDefinitionPOS(getSession().getSessionContext(), value);
    }


    void setDefinitionPOS(SessionContext ctx, int value)
    {
        setDefinitionPOS(ctx, Integer.valueOf(value));
    }


    void setDefinitionPOS(int value)
    {
        setDefinitionPOS(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionParameter.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionParameter.setDescription requires a session language", 0);
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


    public Map<String, String> getAllFilters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "filters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllFilters()
    {
        return getAllFilters(getSession().getSessionContext());
    }


    public void setAllFilters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "filters", value);
    }


    public void setAllFilters(Map<String, String> value)
    {
        setAllFilters(getSession().getSessionContext(), value);
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


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionParameter.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRuleConditionDefinitionParameter.setName requires a session language", 0);
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


    public Boolean isRequired(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "required");
    }


    public Boolean isRequired()
    {
        return isRequired(getSession().getSessionContext());
    }


    public boolean isRequiredAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRequired(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRequiredAsPrimitive()
    {
        return isRequiredAsPrimitive(getSession().getSessionContext());
    }


    public void setRequired(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "required", value);
    }


    public void setRequired(Boolean value)
    {
        setRequired(getSession().getSessionContext(), value);
    }


    public void setRequired(SessionContext ctx, boolean value)
    {
        setRequired(ctx, Boolean.valueOf(value));
    }


    public void setRequired(boolean value)
    {
        setRequired(getSession().getSessionContext(), value);
    }


    public String getType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "type");
    }


    public String getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, String value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(String value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public List<String> getValidators(SessionContext ctx)
    {
        List<String> coll = (List<String>)getProperty(ctx, "validators");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<String> getValidators()
    {
        return getValidators(getSession().getSessionContext());
    }


    public void setValidators(SessionContext ctx, List<String> value)
    {
        setProperty(ctx, "validators", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setValidators(List<String> value)
    {
        setValidators(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
