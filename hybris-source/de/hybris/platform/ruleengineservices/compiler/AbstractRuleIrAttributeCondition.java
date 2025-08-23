package de.hybris.platform.ruleengineservices.compiler;

public abstract class AbstractRuleIrAttributeCondition extends AbstractRuleIrPatternCondition
{
    private String attribute;
    private RuleIrAttributeOperator operator;


    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }


    public String getAttribute()
    {
        return this.attribute;
    }


    public void setOperator(RuleIrAttributeOperator operator)
    {
        this.operator = operator;
    }


    public RuleIrAttributeOperator getOperator()
    {
        return this.operator;
    }
}
