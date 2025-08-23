package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class RuleConditionDefinitionCategoryModel extends ItemModel
{
    public static final String _TYPECODE = "RuleConditionDefinitionCategory";
    public static final String _RULECONDITIONDEFINITION2CATEGORYRELATION = "RuleConditionDefinition2CategoryRelation";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String ICON = "icon";
    public static final String DEFINITIONS = "definitions";


    public RuleConditionDefinitionCategoryModel()
    {
    }


    public RuleConditionDefinitionCategoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleConditionDefinitionCategoryModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "definitions", type = Accessor.Type.GETTER)
    public List<RuleConditionDefinitionModel> getDefinitions()
    {
        return (List<RuleConditionDefinitionModel>)getPersistenceContext().getPropertyValue("definitions");
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
    public CatalogUnawareMediaModel getIcon()
    {
        return (CatalogUnawareMediaModel)getPersistenceContext().getPropertyValue("icon");
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


    @Accessor(qualifier = "definitions", type = Accessor.Type.SETTER)
    public void setDefinitions(List<RuleConditionDefinitionModel> value)
    {
        getPersistenceContext().setPropertyValue("definitions", value);
    }


    @Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
    public void setIcon(CatalogUnawareMediaModel value)
    {
        getPersistenceContext().setPropertyValue("icon", value);
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
}
