package de.hybris.platform.couponservices.order.hooks;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CouponRedemptionMethodHook implements CommercePlaceOrderMethodHook
{
    private CouponService couponService;


    public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", result.getOrder());
        OrderModel order = result.getOrder();
        if(CollectionUtils.isNotEmpty(order.getAppliedCouponCodes()))
        {
            for(String appliedCoupon : order.getAppliedCouponCodes())
            {
                getCouponService().redeemCoupon(appliedCoupon, order);
            }
        }
    }


    public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
    }


    public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    @Required
    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }
}
