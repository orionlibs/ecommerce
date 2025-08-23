package de.hybris.platform.ruleengineservices.rule.data;

import java.util.List;

public class RuleConditionData extends AbstractRuleData
{
    private List<RuleConditionData> children;


    public void setChildren(List<RuleConditionData> children)
    {
        this.children = children;
    }


    public List<RuleConditionData> getChildren()
    {
        return this.children;
    }
}
