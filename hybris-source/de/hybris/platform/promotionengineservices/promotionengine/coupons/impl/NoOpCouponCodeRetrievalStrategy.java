package de.hybris.platform.promotionengineservices.promotionengine.coupons.impl;

import de.hybris.platform.promotionengineservices.promotionengine.coupons.CouponCodeRetrievalStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import java.util.Optional;
import java.util.Set;

public class NoOpCouponCodeRetrievalStrategy implements CouponCodeRetrievalStrategy
{
    public Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel promotion)
    {
        return Optional.empty();
    }
}
