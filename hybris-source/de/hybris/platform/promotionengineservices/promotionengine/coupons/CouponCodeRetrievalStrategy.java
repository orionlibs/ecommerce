package de.hybris.platform.promotionengineservices.promotionengine.coupons;

import de.hybris.platform.promotions.model.PromotionResultModel;
import java.util.Optional;
import java.util.Set;

@FunctionalInterface
public interface CouponCodeRetrievalStrategy
{
    Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel paramPromotionResultModel);
}
