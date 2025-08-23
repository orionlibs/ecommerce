package de.hybris.platform.couponservices.redemption.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import java.util.List;
import java.util.Objects;

public class DefaultSingleCodeCouponRedemptionStrategy implements CouponRedemptionStrategy<SingleCodeCouponModel>
{
    private CouponRedemptionDao couponRedemptionDao;


    public boolean isRedeemable(SingleCodeCouponModel coupon, AbstractOrderModel abstractOrder, String couponCode)
    {
        return isCouponRedeemable(coupon, abstractOrder.getUser(), couponCode);
    }


    public boolean isCouponRedeemable(SingleCodeCouponModel coupon, UserModel user, String couponCode)
    {
        return Objects.nonNull(user) ? checkSingleCodeCouponRedeemableForUser(coupon, user) : checkSingleCodeCouponRedeemable(coupon);
    }


    protected boolean checkSingleCodeCouponRedeemableForUser(SingleCodeCouponModel coupon, UserModel user)
    {
        if(Objects.isNull(coupon.getMaxRedemptionsPerCustomer()))
        {
            return checkSingleCodeCouponRedeemable(coupon);
        }
        int maxRedemptionsPerCustomer = coupon.getMaxRedemptionsPerCustomer().intValue();
        List<CouponRedemptionModel> couponRedemptionsUser = getCouponRedemptionDao().findCouponRedemptionsByCodeAndUser(coupon.getCouponId(), user);
        boolean redeemable = false;
        if(couponRedemptionsUser.size() < maxRedemptionsPerCustomer)
        {
            redeemable = checkSingleCodeCouponRedeemable(coupon);
        }
        return redeemable;
    }


    protected boolean checkSingleCodeCouponRedeemable(SingleCodeCouponModel coupon)
    {
        if(Objects.isNull(coupon.getMaxTotalRedemptions()))
        {
            return true;
        }
        int maxTotalRedemptions = coupon.getMaxTotalRedemptions().intValue();
        int couponRedemptionTotal = getCouponRedemptionDao().findCouponRedemptionsByCouponCode(coupon.getCouponId()).intValue();
        return (couponRedemptionTotal < maxTotalRedemptions);
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }
}
