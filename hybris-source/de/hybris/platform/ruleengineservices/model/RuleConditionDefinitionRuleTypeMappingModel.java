package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RuleConditionDefinitionRuleTypeMappingModel extends ItemModel
{
    public static final String _TYPECODE = "RuleConditionDefinitionRuleTypeMapping";
    public static final String _RULECONDITIONDEFINITION2RULETYPEMAPPINGRELATION = "RuleConditionDefinition2RuleTypeMappingRelation";
    public static final String RULETYPE = "ruleType";
    public static final String DEFINITION = "definition";


    public RuleConditionDefinitionRuleTypeMappingModel()
    {
    }


    public RuleConditionDefinitionRuleTypeMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleConditionDefinitionRuleTypeMappingModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.GETTER)
    public RuleConditionDefinitionModel getDefinition()
    {
        return (RuleConditionDefinitionModel)getPersistenceContext().getPropertyValue("definition");
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getRuleType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("ruleType");
    }


    @Accessor(qualifier = "definition", type = Accessor.Type.SETTER)
    public void setDefinition(RuleConditionDefinitionModel value)
    {
        getPersistenceContext().setPropertyValue("definition", value);
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
    public void setRuleType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("ruleType", value);
    }
}
