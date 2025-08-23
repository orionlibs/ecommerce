package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

public interface RuleParameterUuidGenerator
{
    String generateUuid(RuleParameterData paramRuleParameterData, RuleParameterDefinitionData paramRuleParameterDefinitionData);
}
