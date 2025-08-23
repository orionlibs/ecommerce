package de.hybris.platform.promotionengineservices.compiler.strategies;

import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;

public interface ConditionResolutionStrategy
{
    void getAndStoreParameterValues(RuleConditionData paramRuleConditionData, PromotionSourceRuleModel paramPromotionSourceRuleModel, RuleBasedPromotionModel paramRuleBasedPromotionModel);


    void cleanStoredParameterValues(RuleCompilerContext paramRuleCompilerContext);
}
