package de.hybris.platform.couponservices.cart.hooks;

import de.hybris.platform.commerceservices.order.hook.CommerceCartMergingMethodHook;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeCouponCodeHook implements CommerceCartMergingMethodHook
{
    private CouponService couponService;
    private static final Logger LOG = LoggerFactory.getLogger(MergeCouponCodeHook.class);


    public void afterCartMerge(CartModel fromCart, CartModel toCart)
    {
        if(fromCart.getAppliedCouponCodes() == null || fromCart.getAppliedCouponCodes().isEmpty())
        {
            return;
        }
        for(String couponCode : fromCart.getAppliedCouponCodes())
        {
            CouponResponse response = getCouponService().redeemCoupon(couponCode, toCart);
            if(Boolean.TRUE.equals(response.getSuccess()))
            {
                LOG.info("Applied coupon {} to cart", couponCode);
                continue;
            }
            LOG.warn(response.getMessage());
        }
    }


    public void beforeCartMerge(CartModel fromCart, CartModel toCart)
    {
    }


    public CouponService getCouponService()
    {
        return this.couponService;
    }


    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }
}
