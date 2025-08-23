package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RuleToEngineRuleTypeMappingModel extends ItemModel
{
    public static final String _TYPECODE = "RuleToEngineRuleTypeMapping";
    public static final String RULETYPE = "ruleType";
    public static final String ENGINERULETYPE = "engineRuleType";


    public RuleToEngineRuleTypeMappingModel()
    {
    }


    public RuleToEngineRuleTypeMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleToEngineRuleTypeMappingModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "engineRuleType", type = Accessor.Type.GETTER)
    public RuleType getEngineRuleType()
    {
        return (RuleType)getPersistenceContext().getPropertyValue("engineRuleType");
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getRuleType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("ruleType");
    }


    @Accessor(qualifier = "engineRuleType", type = Accessor.Type.SETTER)
    public void setEngineRuleType(RuleType value)
    {
        getPersistenceContext().setPropertyValue("engineRuleType", value);
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
    public void setRuleType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("ruleType", value);
    }
}
