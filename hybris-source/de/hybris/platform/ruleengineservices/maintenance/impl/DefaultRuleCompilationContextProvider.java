package de.hybris.platform.ruleengineservices.maintenance.impl;

import de.hybris.platform.ruleengine.infrastructure.Prototyped;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContextProvider;

public class DefaultRuleCompilationContextProvider implements RuleCompilationContextProvider
{
    @Prototyped(beanName = "ruleCompilationContext")
    public RuleCompilationContext getRuleCompilationContext()
    {
        return (RuleCompilationContext)new DefaultRuleCompilationContext();
    }
}
