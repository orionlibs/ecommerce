package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGeneratorFactory;

public class DefaultRuleIrVariablesGeneratorFactory implements RuleIrVariablesGeneratorFactory
{
    public RuleIrVariablesGenerator createVariablesGenerator()
    {
        return (RuleIrVariablesGenerator)new DefaultRuleIrVariablesGenerator();
    }
}
