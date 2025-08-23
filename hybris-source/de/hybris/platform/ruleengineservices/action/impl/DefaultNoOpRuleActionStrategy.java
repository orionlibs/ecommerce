package de.hybris.platform.ruleengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.action.RuleActionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.BeanNameAware;

public class DefaultNoOpRuleActionStrategy implements RuleActionStrategy<ItemModel>, BeanNameAware
{
    private String beanName;


    public List<ItemModel> apply(AbstractRuleActionRAO action)
    {
        return Collections.emptyList();
    }


    public void undo(ItemModel action)
    {
    }


    public String getStrategyId()
    {
        return this.beanName;
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }
}
