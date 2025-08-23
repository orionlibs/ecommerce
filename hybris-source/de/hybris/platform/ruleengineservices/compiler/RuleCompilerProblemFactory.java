package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

public interface RuleCompilerProblemFactory
{
    RuleCompilerProblem createProblem(RuleCompilerProblem.Severity paramSeverity, String paramString, Object... paramVarArgs);


    RuleCompilerParameterProblem createParameterProblem(RuleCompilerProblem.Severity paramSeverity, String paramString, RuleParameterData paramRuleParameterData, RuleParameterDefinitionData paramRuleParameterDefinitionData, Object... paramVarArgs);
}
