package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import java.util.List;

public interface RuleCompilerSpliterator<T extends de.hybris.platform.ruleengineservices.model.AbstractRuleModel>
{
    RuleCompilerResult compileSingleRule(T paramT, String paramString);


    RuleCompilerFuture compileRulesAsync(List<T> paramList, String paramString);
}
