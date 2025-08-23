package de.hybris.platform.couponservices.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import java.util.List;

public interface CouponRedemptionDao
{
    List<CouponRedemptionModel> findCouponRedemptionsByCode(String paramString);


    List<CouponRedemptionModel> findCouponRedemptionsByCodeAndOrder(String paramString, AbstractOrderModel paramAbstractOrderModel);


    List<CouponRedemptionModel> findCouponRedemptionsByCodeAndUser(String paramString, UserModel paramUserModel);


    List<CouponRedemptionModel> findCouponRedemptionsByCodeOrderAndUser(String paramString, AbstractOrderModel paramAbstractOrderModel, UserModel paramUserModel);


    Integer findCouponRedemptionsByCouponCode(String paramString);
}
