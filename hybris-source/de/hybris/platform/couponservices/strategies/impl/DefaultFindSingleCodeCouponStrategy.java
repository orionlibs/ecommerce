package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import java.util.Optional;

public class DefaultFindSingleCodeCouponStrategy extends AbstractFindCouponStrategy
{
    protected String getCouponId(String couponCode)
    {
        return couponCode;
    }


    protected Optional<AbstractCouponModel> couponValidation(AbstractCouponModel coupon)
    {
        return (coupon instanceof de.hybris.platform.couponservices.model.SingleCodeCouponModel) ? super.couponValidation(coupon) : Optional.<AbstractCouponModel>empty();
    }
}
