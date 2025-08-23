package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;

public class DefaultRuleCompilerProblem implements RuleCompilerProblem
{
    private final RuleCompilerProblem.Severity severity;
    private final String message;


    public DefaultRuleCompilerProblem(RuleCompilerProblem.Severity severity, String message)
    {
        this.severity = severity;
        this.message = message;
    }


    public RuleCompilerProblem.Severity getSeverity()
    {
        return this.severity;
    }


    public String getMessage()
    {
        return this.message;
    }
}
