package de.hybris.platform.couponservices.redemption.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.redemption.strategies.CouponRedemptionStrategy;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMultiCodeCouponRedemptionStrategy implements CouponRedemptionStrategy<MultiCodeCouponModel>
{
    private CouponCodeGenerationService couponCodeGenerationService;
    private CouponRedemptionDao couponRedemptionDao;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMultiCodeCouponRedemptionStrategy.class);


    public boolean isRedeemable(MultiCodeCouponModel coupon, AbstractOrderModel abstractOrder, String couponCode)
    {
        return checkMultiCodeCouponRedeemable(coupon, couponCode);
    }


    public boolean isCouponRedeemable(MultiCodeCouponModel coupon, UserModel user, String couponCode)
    {
        return checkMultiCodeCouponRedeemable(coupon, couponCode);
    }


    protected boolean checkMultiCodeCouponRedeemable(MultiCodeCouponModel coupon, String couponCode)
    {
        if(getCouponCodeGenerationService().verifyCouponCode(coupon, couponCode))
        {
            if(CollectionUtils.isEmpty(getCouponRedemptionDao().findCouponRedemptionsByCode(couponCode)))
            {
                return true;
            }
            throw new CouponServiceException("coupon.already.redeemed");
        }
        LOG.error("Cannot verify the coupon {} for entered coupon code", coupon.getName());
        throw new CouponServiceException("coupon.invalid.code.provided");
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    @Required
    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }
}
