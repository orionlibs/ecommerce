package de.hybris.platform.customercouponfacades.strategies.impl;

import de.hybris.platform.customercouponfacades.strategies.CustomerCouponRemovableStrategy;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import java.util.Calendar;

public class DefaultCustomerCouponRemovableStrategy implements CustomerCouponRemovableStrategy
{
    private CustomerCouponService customerCouponService;


    public boolean checkRemovable(String couponCode)
    {
        return ((Boolean)getCustomerCouponService()
                        .getCouponForCode(couponCode)
                        .map(coupon -> Boolean.valueOf((coupon.getEndDate() != null && coupon.getEndDate().after(Calendar.getInstance().getTime()))))
                        .orElse(Boolean.FALSE)).booleanValue();
    }


    protected CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }
}
