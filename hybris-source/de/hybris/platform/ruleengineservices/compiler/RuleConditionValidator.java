package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

public interface RuleConditionValidator
{
    void validate(RuleCompilerContext paramRuleCompilerContext, RuleConditionData paramRuleConditionData, RuleConditionDefinitionData paramRuleConditionDefinitionData);
}
