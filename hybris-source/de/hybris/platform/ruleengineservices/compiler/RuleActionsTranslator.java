package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import java.util.List;

public interface RuleActionsTranslator
{
    void validate(RuleCompilerContext paramRuleCompilerContext, List<RuleActionData> paramList);


    List<RuleIrAction> translate(RuleCompilerContext paramRuleCompilerContext, List<RuleActionData> paramList);
}
