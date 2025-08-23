package de.hybris.platform.ruleengine.impl;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import java.util.function.Function;

public class DefaultMaxRuleExecutionsFunction implements Function<RuleEvaluationContext, Integer>
{
    public Integer apply(RuleEvaluationContext context)
    {
        return Integer.valueOf(-1);
    }
}
