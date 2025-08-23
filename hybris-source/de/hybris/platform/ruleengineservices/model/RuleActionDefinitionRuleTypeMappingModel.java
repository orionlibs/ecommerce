package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RuleActionDefinitionRuleTypeMappingModel extends ItemModel
{
    public static final String _TYPECODE = "RuleActionDefinitionRuleTypeMapping";
    public static final String _RULEACTIONDEFINITION2RULETYPEMAPPINGRELATION = "RuleActionDefinition2RuleTypeMappingRelation";
    public static final String RULETYPE = "ruleType";
    public static final String DEFINITION = "definition";


    public RuleActionDefinitionRuleTypeMappingModel()
    {
    }


    public RuleActionDefinitionRuleTypeMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleActionDefinitionRuleTypeMappingModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.GETTER)
    public RuleActionDefinitionModel getDefinition()
    {
        return (RuleActionDefinitionModel)getPersistenceContext().getPropertyValue("definition");
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getRuleType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("ruleType");
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.SETTER)
    public void setDefinition(RuleActionDefinitionModel value)
    {
        getPersistenceContext().setPropertyValue("definition", value);
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
    public void setRuleType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("ruleType", value);
    }
}
