package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerParameterProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

public class DefaultRuleCompilerParameterProblem extends DefaultRuleCompilerProblem implements RuleCompilerParameterProblem
{
    private final RuleParameterData parameter;
    private final RuleParameterDefinitionData parameterDefinition;


    public DefaultRuleCompilerParameterProblem(RuleCompilerProblem.Severity severity, String message, RuleParameterData parameter, RuleParameterDefinitionData parameterDefinition)
    {
        super(severity, message);
        this.parameter = parameter;
        this.parameterDefinition = parameterDefinition;
    }


    public RuleParameterData getParameter()
    {
        return this.parameter;
    }


    public RuleParameterDefinitionData getParameterDefinition()
    {
        return this.parameterDefinition;
    }
}
