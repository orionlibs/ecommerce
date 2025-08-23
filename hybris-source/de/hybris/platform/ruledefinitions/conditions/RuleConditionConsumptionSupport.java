package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import java.util.List;
import javax.annotation.Nonnull;

public interface RuleConditionConsumptionSupport
{
    List<RuleIrCondition> newProductConsumedCondition(@Nonnull RuleCompilerContext paramRuleCompilerContext, String paramString);
}
