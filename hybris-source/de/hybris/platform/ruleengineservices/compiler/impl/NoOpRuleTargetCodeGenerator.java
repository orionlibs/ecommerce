package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGenerator;

public class NoOpRuleTargetCodeGenerator implements RuleTargetCodeGenerator
{
    public void generate(RuleCompilerContext context, RuleIr ruleIr)
    {
    }
}
