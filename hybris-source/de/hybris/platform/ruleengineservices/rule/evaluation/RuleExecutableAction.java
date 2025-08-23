package de.hybris.platform.ruleengineservices.rule.evaluation;

import java.util.Map;

public interface RuleExecutableAction
{
    void executeAction(RuleActionContext paramRuleActionContext, Map<String, Object> paramMap);
}
