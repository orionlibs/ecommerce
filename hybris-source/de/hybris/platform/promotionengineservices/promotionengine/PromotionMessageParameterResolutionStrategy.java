package de.hybris.platform.promotionengineservices.promotionengine;

import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Locale;

public interface PromotionMessageParameterResolutionStrategy
{
    Object getValue(RuleParameterData paramRuleParameterData, PromotionResultModel paramPromotionResultModel, Locale paramLocale);


    default RuleParameterData getReplacedParameter(RuleParameterData paramToReplace, PromotionResultModel promotionResult, Object actualValueAsObject)
    {
        return paramToReplace;
    }
}
