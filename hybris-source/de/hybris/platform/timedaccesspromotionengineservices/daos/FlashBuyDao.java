package de.hybris.platform.timedaccesspromotionengineservices.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.List;
import java.util.Optional;

public interface FlashBuyDao
{
    Optional<FlashBuyCouponModel> findFlashBuyCouponByPromotionCode(String paramString);


    List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(String paramString);


    Optional<ProductModel> findProductByPromotion(AbstractPromotionModel paramAbstractPromotionModel);


    List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRules(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    List<ProductModel> findAllProductsByPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    List<FlashBuyCouponModel> findFlashBuyCouponByProduct(ProductModel paramProductModel);
}
