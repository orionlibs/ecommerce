package de.hybris.platform.ruleengineservices.rule.evaluation.impl;

public interface RuleAndRuleGroupExecutionTracker
{
    boolean allowedToExecute(Object paramObject);


    void trackActionExecutionStarted(String paramString);


    void trackRuleExecution(Object paramObject);
}
