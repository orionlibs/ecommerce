package de.hybris.platform.ruledefinitions.conditions.builders;

import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;

public class RuleIrAttributeConditionBuilder
{
    private static RuleIrAttributeConditionBuilder self = new RuleIrAttributeConditionBuilder();
    private RuleIrAttributeCondition condition;


    public static RuleIrAttributeConditionBuilder newAttributeConditionFor(String variableName)
    {
        self.condition = new RuleIrAttributeCondition();
        self.condition.setVariable(variableName);
        return self;
    }


    public RuleIrAttributeConditionBuilder withAttribute(String attribute)
    {
        self.condition.setAttribute(attribute);
        return self;
    }


    public RuleIrAttributeConditionBuilder withOperator(RuleIrAttributeOperator operator)
    {
        self.condition.setOperator(operator);
        return self;
    }


    public RuleIrAttributeConditionBuilder withValue(Object value)
    {
        self.condition.setValue(value);
        return self;
    }


    public RuleIrAttributeCondition build()
    {
        return this.condition;
    }
}
