package de.hybris.platform.ruleengineservices.rule.data;

import java.util.List;

public class RuleConditionDefinitionData extends AbstractRuleDefinitionData
{
    private Boolean allowsChildren;
    private List<RuleConditionDefinitionCategoryData> categories;


    public void setAllowsChildren(Boolean allowsChildren)
    {
        this.allowsChildren = allowsChildren;
    }


    public Boolean getAllowsChildren()
    {
        return this.allowsChildren;
    }


    public void setCategories(List<RuleConditionDefinitionCategoryData> categories)
    {
        this.categories = categories;
    }


    public List<RuleConditionDefinitionCategoryData> getCategories()
    {
        return this.categories;
    }
}
