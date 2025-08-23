package de.hybris.platform.ruleengineservices.rule.evaluation;

import java.util.Map;

public interface RuleExecutableCondition
{
    boolean evaluateCondition(RuleConditionContext paramRuleConditionContext, Map<String, Object> paramMap);
}
