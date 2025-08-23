package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;

public interface RuleActionTranslator
{
    RuleIrAction translate(RuleCompilerContext paramRuleCompilerContext, RuleActionData paramRuleActionData, RuleActionDefinitionData paramRuleActionDefinitionData);
}
