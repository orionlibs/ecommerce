package de.hybris.platform.ruleengineservices.action;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.util.List;

public interface RuleActionService
{
    List<ItemModel> applyAllActions(RuleEngineResultRAO paramRuleEngineResultRAO);
}
