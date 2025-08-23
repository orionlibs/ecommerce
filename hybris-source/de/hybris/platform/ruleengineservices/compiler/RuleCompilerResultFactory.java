package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.List;

public interface RuleCompilerResultFactory
{
    RuleCompilerResult create(AbstractRuleModel paramAbstractRuleModel, RuleCompilerResult.Result paramResult, List<RuleCompilerProblem> paramList);


    RuleCompilerResult create(AbstractRuleModel paramAbstractRuleModel, List<RuleCompilerProblem> paramList);


    RuleCompilerResult create(RuleCompilerResult paramRuleCompilerResult, long paramLong);
}
