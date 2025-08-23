package de.hybris.platform.promotionengineservices.dao;

import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.CombinedCatsForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedCatForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedProductForRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PromotionSourceRuleDao
{
    List<ProductForPromotionSourceRuleModel> findAllProductForPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<CatForPromotionSourceRuleModel> findAllCatForPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<ExcludedCatForRuleModel> findAllExcludedCatForPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<ExcludedProductForRuleModel> findAllExcludedProductForPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<CombinedCatsForRuleModel> findAllCombinedCatsForRule(PromotionSourceRuleModel paramPromotionSourceRuleModel, String paramString);


    List<RuleBasedPromotionModel> findPromotions(Collection<PromotionGroupModel> paramCollection, String paramString, Set<String> paramSet);


    Integer findLastConditionIdForRule(PromotionSourceRuleModel paramPromotionSourceRuleModel);
}
