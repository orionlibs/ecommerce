package de.hybris.platform.couponservices.redemption.strategies;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;

public interface CouponRedemptionStrategy<T extends de.hybris.platform.couponservices.model.AbstractCouponModel>
{
    boolean isRedeemable(T paramT, AbstractOrderModel paramAbstractOrderModel, String paramString);


    boolean isCouponRedeemable(T paramT, UserModel paramUserModel, String paramString);
}
