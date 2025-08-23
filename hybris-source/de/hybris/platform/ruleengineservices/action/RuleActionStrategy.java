package de.hybris.platform.ruleengineservices.action;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import java.util.List;

public interface RuleActionStrategy<T extends ItemModel>
{
    List<T> apply(AbstractRuleActionRAO paramAbstractRuleActionRAO);


    String getStrategyId();


    void undo(ItemModel paramItemModel);
}
