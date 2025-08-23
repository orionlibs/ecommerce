package de.hybris.platform.customercouponservices.redemption.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;

public class DefaultCustomerCouponRedemptionStrategy implements CouponRedemptionStrategy<CustomerCouponModel>
{
    private CustomerCouponDao customerCouponDao;


    public boolean isRedeemable(CustomerCouponModel coupon, AbstractOrderModel abstractOrder, String couponCode)
    {
        return isCouponRedeemable(coupon, abstractOrder.getUser(), couponCode);
    }


    public boolean isCouponRedeemable(CustomerCouponModel coupon, UserModel user, String couponCode)
    {
        if(!coupon.getCustomers().contains(user))
        {
            throw new CouponServiceException("coupon.invalid.code.provided");
        }
        return getCustomerCouponDao().checkCustomerCouponAvailableForCustomer(couponCode, (CustomerModel)user);
    }


    protected CustomerCouponDao getCustomerCouponDao()
    {
        return this.customerCouponDao;
    }


    public void setCustomerCouponDao(CustomerCouponDao customerCouponDao)
    {
        this.customerCouponDao = customerCouponDao;
    }
}
