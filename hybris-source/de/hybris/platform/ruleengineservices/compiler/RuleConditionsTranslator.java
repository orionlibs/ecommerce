package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import java.util.List;

public interface RuleConditionsTranslator
{
    void validate(RuleCompilerContext paramRuleCompilerContext, List<RuleConditionData> paramList);


    List<RuleIrCondition> translate(RuleCompilerContext paramRuleCompilerContext, List<RuleConditionData> paramList);
}
