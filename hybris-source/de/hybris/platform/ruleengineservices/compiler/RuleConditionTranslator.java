package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

public interface RuleConditionTranslator
{
    RuleIrCondition translate(RuleCompilerContext paramRuleCompilerContext, RuleConditionData paramRuleConditionData, RuleConditionDefinitionData paramRuleConditionDefinitionData);
}
