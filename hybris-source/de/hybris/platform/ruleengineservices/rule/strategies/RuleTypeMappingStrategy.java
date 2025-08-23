package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;

public interface RuleTypeMappingStrategy
{
    Class<? extends AbstractRuleModel> findRuleType(Class<? extends AbstractRuleTemplateModel> paramClass);
}
