package de.hybris.platform.customercouponservices.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;

public interface CustomerCouponsPlaceOrderStrategy
{
    void removeCouponsForCustomer(UserModel paramUserModel, OrderModel paramOrderModel);


    void updateContinueUrl();
}
