package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import java.util.List;
import java.util.Objects;

public class DefaultRuleCompilerResult implements RuleCompilerResult
{
    private final String ruleCode;
    private long ruleVersion;
    private final RuleCompilerResult.Result result;
    private final List<RuleCompilerProblem> problems;


    public DefaultRuleCompilerResult(String ruleCode, RuleCompilerResult.Result result, List<RuleCompilerProblem> problems)
    {
        this.ruleCode = ruleCode;
        this.result = result;
        this.problems = problems;
    }


    public DefaultRuleCompilerResult(String ruleCode, List<RuleCompilerProblem> problems)
    {
        this.ruleCode = ruleCode;
        this.problems = problems;
        if(problems != null)
        {
            Objects.requireNonNull(RuleCompilerProblem.Severity.ERROR);
            this
                            .result = problems.stream().map(RuleCompilerProblem::getSeverity).anyMatch(RuleCompilerProblem.Severity.ERROR::equals) ? RuleCompilerResult.Result.ERROR : RuleCompilerResult.Result.SUCCESS;
        }
        else
        {
            this.result = RuleCompilerResult.Result.SUCCESS;
        }
    }


    public DefaultRuleCompilerResult(String ruleCode, RuleCompilerResult.Result result, List<RuleCompilerProblem> problems, long ruleVersion)
    {
        this(ruleCode, result, problems);
        this.ruleVersion = ruleVersion;
    }


    public String getRuleCode()
    {
        return this.ruleCode;
    }


    public long getRuleVersion()
    {
        return this.ruleVersion;
    }


    public RuleCompilerResult.Result getResult()
    {
        return this.result;
    }


    public List<RuleCompilerProblem> getProblems()
    {
        return this.problems;
    }
}
