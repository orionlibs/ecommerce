package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

public interface RuleCompilerContextFactory<T extends RuleCompilerContext>
{
    T createContext(RuleCompilationContext paramRuleCompilationContext, AbstractRuleModel paramAbstractRuleModel, String paramString, RuleIrVariablesGenerator paramRuleIrVariablesGenerator);
}
