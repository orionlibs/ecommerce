package de.hybris.platform.timedaccesspromotionengineservices;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.List;
import java.util.Optional;

public interface FlashBuyService
{
    Optional<ProductModel> getProductForPromotion(AbstractPromotionModel paramAbstractPromotionModel);


    List<PromotionSourceRuleModel> getPromotionSourceRulesByProductCode(String paramString);


    Optional<FlashBuyCouponModel> getFlashBuyCouponByPromotionCode(String paramString);


    void undeployFlashBuyPromotion(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    AbstractPromotionModel getPromotionByCode(String paramString);


    void createCronJobForFlashBuyCoupon(FlashBuyCouponModel paramFlashBuyCouponModel);


    void performFlashBuyCronJob(FlashBuyCouponModel paramFlashBuyCouponModel);


    List<ProductForPromotionSourceRuleModel> getProductForPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    List<ProductModel> getProductForCode(String paramString);


    List<ProductModel> getAllProductsByPromotionSourceRule(PromotionSourceRuleModel paramPromotionSourceRuleModel);


    List<FlashBuyCouponModel> getFlashBuyCouponByProduct(ProductModel paramProductModel);


    void deleteCronJobAndTrigger(FlashBuyCouponModel paramFlashBuyCouponModel);
}
