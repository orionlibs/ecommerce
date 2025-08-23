package de.hybris.platform.ruleengineservices.compiler;

public class RuleIrNotCondition extends RuleIrConditionWithChildren
{
    private RuleIrLocalVariablesContainer variablesContainer;


    public void setVariablesContainer(RuleIrLocalVariablesContainer variablesContainer)
    {
        this.variablesContainer = variablesContainer;
    }


    public RuleIrLocalVariablesContainer getVariablesContainer()
    {
        return this.variablesContainer;
    }
}
