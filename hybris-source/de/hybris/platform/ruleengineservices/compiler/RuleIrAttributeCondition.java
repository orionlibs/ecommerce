package de.hybris.platform.ruleengineservices.compiler;

public class RuleIrAttributeCondition extends AbstractRuleIrAttributeCondition
{
    private Object value;


    public void setValue(Object value)
    {
        this.value = value;
    }


    public Object getValue()
    {
        return this.value;
    }
}
