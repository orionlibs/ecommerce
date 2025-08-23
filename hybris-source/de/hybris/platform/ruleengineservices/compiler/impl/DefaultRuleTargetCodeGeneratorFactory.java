package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGeneratorFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleTargetCodeGeneratorFactory implements RuleTargetCodeGeneratorFactory
{
    private RuleTargetCodeGenerator ruleTargetCodeGenerator;


    public RuleTargetCodeGenerator getRuleTargetCodeGenerator()
    {
        return this.ruleTargetCodeGenerator;
    }


    @Required
    public void setRuleTargetCodeGenerator(RuleTargetCodeGenerator ruleTargetCodeGenerator)
    {
        this.ruleTargetCodeGenerator = ruleTargetCodeGenerator;
    }


    public RuleTargetCodeGenerator getTargetCodeGenerator(RuleCompilerContext context)
    {
        return this.ruleTargetCodeGenerator;
    }
}
