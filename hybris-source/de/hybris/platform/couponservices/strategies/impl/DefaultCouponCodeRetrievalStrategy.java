package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.platform.couponservices.model.RuleBasedAddCouponActionModel;
import de.hybris.platform.promotionengineservices.promotionengine.coupons.CouponCodeRetrievalStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCouponCodeRetrievalStrategy implements CouponCodeRetrievalStrategy
{
    public Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel promotionResult)
    {
        if(promotionResult.getPromotion() instanceof de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel)
        {
            return Optional.of((Set<String>)((Set)Optional.<Set>ofNullable(promotionResult.getAllPromotionActions()).orElse(Collections.emptySet())).stream()
                            .filter(action -> action instanceof RuleBasedAddCouponActionModel)
                            .map(action -> ((RuleBasedAddCouponActionModel)action).getCouponCode()).collect(Collectors.toSet()));
        }
        return Optional.empty();
    }
}
