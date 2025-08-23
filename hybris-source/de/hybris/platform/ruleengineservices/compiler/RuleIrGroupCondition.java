package de.hybris.platform.ruleengineservices.compiler;

public class RuleIrGroupCondition extends RuleIrConditionWithChildren
{
    private RuleIrGroupOperator operator;


    public void setOperator(RuleIrGroupOperator operator)
    {
        this.operator = operator;
    }


    public RuleIrGroupOperator getOperator()
    {
        return this.operator;
    }
}
