package de.hybris.platform.ruleengineservices.rule.data;

import java.util.List;

public class RuleActionDefinitionData extends AbstractRuleDefinitionData
{
    private List<RuleActionDefinitionCategoryData> categories;


    public void setCategories(List<RuleActionDefinitionCategoryData> categories)
    {
        this.categories = categories;
    }


    public List<RuleActionDefinitionCategoryData> getCategories()
    {
        return this.categories;
    }
}
