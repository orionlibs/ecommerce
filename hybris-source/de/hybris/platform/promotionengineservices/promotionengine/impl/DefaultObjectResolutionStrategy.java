package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Locale;

public class DefaultObjectResolutionStrategy implements PromotionMessageParameterResolutionStrategy
{
    public String getValue(RuleParameterData data, PromotionResultModel promotionResult, Locale locale)
    {
        ServicesUtil.validateParameterNotNull(data, "data must not be null");
        ServicesUtil.validateParameterNotNull(data.getValue(), "data value must not be null");
        return data.getValue().toString();
    }


    public RuleParameterData getReplacedParameter(RuleParameterData paramToReplace, PromotionResultModel promotionResult, Object actualValueAsObject)
    {
        ServicesUtil.validateParameterNotNull(paramToReplace, "parameter paramToReplace must not be null");
        ServicesUtil.validateParameterNotNull(actualValueAsObject, "parameter actualValueAsObject must not be null");
        RuleParameterData result = new RuleParameterData();
        result.setType(paramToReplace.getType());
        result.setUuid(paramToReplace.getUuid());
        result.setValue(actualValueAsObject);
        return result;
    }
}
