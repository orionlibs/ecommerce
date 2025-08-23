package de.hybris.platform.couponservices.cart.hooks;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class CouponRedeemableValidationHook implements CartValidationHook
{
    private static final String COUPONNOTVALID = "couponNotValid";
    private CouponService couponService;


    public void beforeValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
    }


    public void afterValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("cart", parameter.getCart());
        CartModel cartModel = parameter.getCart();
        if(CollectionUtils.isNotEmpty(cartModel.getAppliedCouponCodes()))
        {
            for(String couponCode : cartModel.getAppliedCouponCodes())
            {
                CouponResponse response = getCouponService().verifyCouponCode(couponCode, (AbstractOrderModel)cartModel);
                if(BooleanUtils.isFalse(response.getSuccess()))
                {
                    getCouponService().releaseCouponCode(couponCode, (AbstractOrderModel)cartModel);
                    CommerceCartModification cartModificationData = new CommerceCartModification();
                    cartModificationData.setStatusCode("couponNotValid");
                    modifications.add(cartModificationData);
                }
            }
        }
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
