package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

public interface RuleParameterValidator
{
    void validate(RuleCompilerContext paramRuleCompilerContext, AbstractRuleDefinitionData paramAbstractRuleDefinitionData, RuleParameterData paramRuleParameterData, RuleParameterDefinitionData paramRuleParameterDefinitionData);
}
