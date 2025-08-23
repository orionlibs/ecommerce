package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslatorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslatorNotFoundException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleSourceCodeTranslatorFactory implements RuleSourceCodeTranslatorFactory
{
    private RuleSourceCodeTranslator sourceRuleSourceCodeTranslator;


    public RuleSourceCodeTranslator getSourceCodeTranslator(RuleCompilerContext context)
    {
        if(context.getRule() instanceof de.hybris.platform.ruleengineservices.model.SourceRuleModel)
        {
            return this.sourceRuleSourceCodeTranslator;
        }
        throw new RuleSourceCodeTranslatorNotFoundException("Source code translator not found for rule: " + context.getRule());
    }


    public RuleSourceCodeTranslator getSourceRuleSourceCodeTranslator()
    {
        return this.sourceRuleSourceCodeTranslator;
    }


    @Required
    public void setSourceRuleSourceCodeTranslator(RuleSourceCodeTranslator sourceRuleSourceCodeTranslator)
    {
        this.sourceRuleSourceCodeTranslator = sourceRuleSourceCodeTranslator;
    }
}
