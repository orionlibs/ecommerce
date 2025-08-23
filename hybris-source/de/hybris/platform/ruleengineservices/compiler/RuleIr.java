package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;
import java.util.List;

public class RuleIr implements Serializable
{
    private static final long serialVersionUID = 1L;
    private RuleIrVariablesContainer variablesContainer;
    private List<RuleIrCondition> conditions;
    private List<RuleIrAction> actions;


    public void setVariablesContainer(RuleIrVariablesContainer variablesContainer)
    {
        this.variablesContainer = variablesContainer;
    }


    public RuleIrVariablesContainer getVariablesContainer()
    {
        return this.variablesContainer;
    }


    public void setConditions(List<RuleIrCondition> conditions)
    {
        this.conditions = conditions;
    }


    public List<RuleIrCondition> getConditions()
    {
        return this.conditions;
    }


    public void setActions(List<RuleIrAction> actions)
    {
        this.actions = actions;
    }


    public List<RuleIrAction> getActions()
    {
        return this.actions;
    }
}
