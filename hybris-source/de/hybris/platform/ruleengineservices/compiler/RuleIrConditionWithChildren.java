package de.hybris.platform.ruleengineservices.compiler;

import java.util.List;

public class RuleIrConditionWithChildren extends RuleIrCondition
{
    private List<RuleIrCondition> children;


    public void setChildren(List<RuleIrCondition> children)
    {
        this.children = children;
    }


    public List<RuleIrCondition> getChildren()
    {
        return this.children;
    }
}
