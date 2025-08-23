package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RuleActionDefinitionParameterModel extends ItemModel
{
    public static final String _TYPECODE = "RuleActionDefinitionParameter";
    public static final String _RULEACTIONDEFINITION2PARAMETERSRELATION = "RuleActionDefinition2ParametersRelation";
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


    public RuleActionDefinitionParameterModel()
    {
    }


    public RuleActionDefinitionParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleActionDefinitionParameterModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "defaultEditor", type = Accessor.Type.GETTER)
    public String getDefaultEditor()
    {
        return (String)getPersistenceContext().getPropertyValue("defaultEditor");
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.GETTER)
    public RuleActionDefinitionModel getDefinition()
    {
        return (RuleActionDefinitionModel)getPersistenceContext().getPropertyValue("definition");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "filters", type = Accessor.Type.GETTER)
    public Map<String, String> getFilters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("filters");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "required", type = Accessor.Type.GETTER)
    public Boolean getRequired()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("required");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return (String)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "validators", type = Accessor.Type.GETTER)
    public List<String> getValidators()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("validators");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "defaultEditor", type = Accessor.Type.SETTER)
    public void setDefaultEditor(String value)
    {
        getPersistenceContext().setPropertyValue("defaultEditor", value);
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.SETTER)
    public void setDefinition(RuleActionDefinitionModel value)
    {
        getPersistenceContext().setPropertyValue("definition", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "filters", type = Accessor.Type.SETTER)
    public void setFilters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("filters", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "required", type = Accessor.Type.SETTER)
    public void setRequired(Boolean value)
    {
        getPersistenceContext().setPropertyValue("required", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(String value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "validators", type = Accessor.Type.SETTER)
    public void setValidators(List<String> value)
    {
        getPersistenceContext().setPropertyValue("validators", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
