package de.hybris.platform.ruledefinitions.conditions.builders;

import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeRelCondition;

public class RuleIrAttributeRelConditionBuilder
{
    private static RuleIrAttributeRelConditionBuilder self = new RuleIrAttributeRelConditionBuilder();
    private RuleIrAttributeRelCondition condition;


    public static RuleIrAttributeRelConditionBuilder newAttributeRelationConditionFor(String variableName)
    {
        self.condition = new RuleIrAttributeRelCondition();
        self.condition.setVariable(variableName);
        return self;
    }


    public RuleIrAttributeRelConditionBuilder withAttribute(String attribute)
    {
        self.condition.setAttribute(attribute);
        return self;
    }


    public RuleIrAttributeRelConditionBuilder withOperator(RuleIrAttributeOperator operator)
    {
        self.condition.setOperator(operator);
        return self;
    }


    public RuleIrAttributeRelConditionBuilder withTargetVariable(String value)
    {
        self.condition.setTargetVariable(value);
        return self;
    }


    public RuleIrAttributeRelConditionBuilder withTargetAttribute(String targetAttribute)
    {
        self.condition.setTargetAttribute(targetAttribute);
        return self;
    }


    public RuleIrAttributeRelCondition build()
    {
        return this.condition;
    }
}
