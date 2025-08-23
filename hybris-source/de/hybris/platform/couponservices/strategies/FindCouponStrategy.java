package de.hybris.platform.couponservices.strategies;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import java.util.Optional;

public interface FindCouponStrategy
{
    Optional<AbstractCouponModel> findCouponForCouponCode(String paramString);


    Optional<AbstractCouponModel> findValidatedCouponForCouponCode(String paramString);
}
