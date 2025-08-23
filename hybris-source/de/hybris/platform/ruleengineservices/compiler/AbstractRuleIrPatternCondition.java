package de.hybris.platform.ruleengineservices.compiler;

public abstract class AbstractRuleIrPatternCondition extends RuleIrCondition
{
    private String variable;


    public void setVariable(String variable)
    {
        this.variable = variable;
    }


    public String getVariable()
    {
        return this.variable;
    }
}
