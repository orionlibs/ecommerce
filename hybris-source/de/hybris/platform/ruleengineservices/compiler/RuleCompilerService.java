package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

public interface RuleCompilerService
{
    RuleCompilerResult compile(RuleCompilationContext paramRuleCompilationContext, AbstractRuleModel paramAbstractRuleModel, String paramString);


    RuleCompilerResult compile(AbstractRuleModel paramAbstractRuleModel, String paramString);
}
