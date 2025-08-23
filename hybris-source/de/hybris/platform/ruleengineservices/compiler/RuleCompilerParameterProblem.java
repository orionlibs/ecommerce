package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

public interface RuleCompilerParameterProblem extends RuleCompilerProblem
{
    RuleParameterData getParameter();


    RuleParameterDefinitionData getParameterDefinition();
}
