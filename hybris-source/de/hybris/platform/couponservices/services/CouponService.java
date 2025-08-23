package de.hybris.platform.couponservices.services;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import java.util.Optional;

public interface CouponService
{
    void releaseCouponCode(String paramString, AbstractOrderModel paramAbstractOrderModel);


    CouponResponse verifyCouponCode(String paramString, AbstractOrderModel paramAbstractOrderModel);


    CouponResponse validateCouponCode(String paramString, UserModel paramUserModel);


    Optional<AbstractCouponModel> getCouponForCode(String paramString);


    Optional<AbstractCouponModel> getValidatedCouponForCode(String paramString);


    CouponResponse redeemCoupon(String paramString, CartModel paramCartModel);


    CouponResponse redeemCoupon(String paramString, OrderModel paramOrderModel);
}
