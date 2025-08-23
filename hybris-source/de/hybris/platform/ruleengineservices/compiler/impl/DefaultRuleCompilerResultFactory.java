package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResultFactory;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.List;

public class DefaultRuleCompilerResultFactory implements RuleCompilerResultFactory
{
    public RuleCompilerResult create(AbstractRuleModel rule, RuleCompilerResult.Result result, List<RuleCompilerProblem> problems)
    {
        return (RuleCompilerResult)new DefaultRuleCompilerResult(rule.getCode(), result, problems);
    }


    public RuleCompilerResult create(AbstractRuleModel rule, List<RuleCompilerProblem> problems)
    {
        return (RuleCompilerResult)new DefaultRuleCompilerResult(rule.getCode(), problems);
    }


    public RuleCompilerResult create(RuleCompilerResult compilerResult, long ruleVersion)
    {
        return (RuleCompilerResult)new DefaultRuleCompilerResult(compilerResult.getRuleCode(), compilerResult.getResult(), compilerResult.getProblems(), ruleVersion);
    }
}
