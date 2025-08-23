package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SourceRuleTemplateModel extends AbstractRuleTemplateModel
{
    public static final String _TYPECODE = "SourceRuleTemplate";
    public static final String CONDITIONS = "conditions";
    public static final String ACTIONS = "actions";


    public SourceRuleTemplateModel()
    {
    }


    public SourceRuleTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SourceRuleTemplateModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SourceRuleTemplateModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public String getActions()
    {
        return (String)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
    public String getConditions()
    {
        return (String)getPersistenceContext().getPropertyValue("conditions");
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(String value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
    public void setConditions(String value)
    {
        getPersistenceContext().setPropertyValue("conditions", value);
    }
}
