package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RuleConditionDefinitionModel extends ItemModel
{
    public static final String _TYPECODE = "RuleConditionDefinition";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String BREADCRUMB = "breadcrumb";
    public static final String ALLOWSCHILDREN = "allowsChildren";
    public static final String TRANSLATORID = "translatorId";
    public static final String TRANSLATORPARAMETERS = "translatorParameters";
    public static final String CATEGORIES = "categories";
    public static final String PARAMETERS = "parameters";
    public static final String RULETYPES = "ruleTypes";


    public RuleConditionDefinitionModel()
    {
    }


    public RuleConditionDefinitionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleConditionDefinitionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "allowsChildren", type = Accessor.Type.GETTER)
    public Boolean getAllowsChildren()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("allowsChildren");
    }


    @Accessor(qualifier = "breadcrumb", type = Accessor.Type.GETTER)
    public String getBreadcrumb()
    {
        return getBreadcrumb(null);
    }


    @Accessor(qualifier = "breadcrumb", type = Accessor.Type.GETTER)
    public String getBreadcrumb(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("breadcrumb", loc);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
    public List<RuleConditionDefinitionCategoryModel> getCategories()
    {
        return (List<RuleConditionDefinitionCategoryModel>)getPersistenceContext().getPropertyValue("categories");
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


    @Accessor(qualifier = "parameters", type = Accessor.Type.GETTER)
    public List<RuleConditionDefinitionParameterModel> getParameters()
    {
        return (List<RuleConditionDefinitionParameterModel>)getPersistenceContext().getPropertyValue("parameters");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "ruleTypes", type = Accessor.Type.GETTER)
    public List<RuleConditionDefinitionRuleTypeMappingModel> getRuleTypes()
    {
        return (List<RuleConditionDefinitionRuleTypeMappingModel>)getPersistenceContext().getPropertyValue("ruleTypes");
    }


    @Accessor(qualifier = "translatorId", type = Accessor.Type.GETTER)
    public String getTranslatorId()
    {
        return (String)getPersistenceContext().getPropertyValue("translatorId");
    }


    @Accessor(qualifier = "translatorParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getTranslatorParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("translatorParameters");
    }


    @Accessor(qualifier = "allowsChildren", type = Accessor.Type.SETTER)
    public void setAllowsChildren(Boolean value)
    {
        getPersistenceContext().setPropertyValue("allowsChildren", value);
    }


    @Accessor(qualifier = "breadcrumb", type = Accessor.Type.SETTER)
    public void setBreadcrumb(String value)
    {
        setBreadcrumb(value, null);
    }


    @Accessor(qualifier = "breadcrumb", type = Accessor.Type.SETTER)
    public void setBreadcrumb(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("breadcrumb", loc, value);
    }


    @Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
    public void setCategories(List<RuleConditionDefinitionCategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("categories", value);
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


    @Accessor(qualifier = "parameters", type = Accessor.Type.SETTER)
    public void setParameters(List<RuleConditionDefinitionParameterModel> value)
    {
        getPersistenceContext().setPropertyValue("parameters", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "ruleTypes", type = Accessor.Type.SETTER)
    public void setRuleTypes(List<RuleConditionDefinitionRuleTypeMappingModel> value)
    {
        getPersistenceContext().setPropertyValue("ruleTypes", value);
    }


    @Accessor(qualifier = "translatorId", type = Accessor.Type.SETTER)
    public void setTranslatorId(String value)
    {
        getPersistenceContext().setPropertyValue("translatorId", value);
    }


    @Accessor(qualifier = "translatorParameters", type = Accessor.Type.SETTER)
    public void setTranslatorParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("translatorParameters", value);
    }
}
