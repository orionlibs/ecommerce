package de.hybris.platform.promotionengineservices.validators;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.enums.RuleType;

public interface RuleBasedPromotionsContextValidator
{
    boolean isApplicable(RuleBasedPromotionModel paramRuleBasedPromotionModel, CatalogVersionModel paramCatalogVersionModel, RuleType paramRuleType);
}
