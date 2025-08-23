package de.hybris.platform.customercouponservices.strategies.impl;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.strategies.impl.AbstractFindCouponStrategy;
import java.util.Optional;

public class DefaultFindCustomerCouponStrategy extends AbstractFindCouponStrategy
{
    protected Optional<AbstractCouponModel> couponValidation(AbstractCouponModel coupon)
    {
        return (coupon instanceof de.hybris.platform.customercouponservices.model.CustomerCouponModel) ? super.couponValidation(coupon) : Optional.<AbstractCouponModel>empty();
    }


    protected String getCouponId(String couponCode)
    {
        return couponCode;
    }
}
