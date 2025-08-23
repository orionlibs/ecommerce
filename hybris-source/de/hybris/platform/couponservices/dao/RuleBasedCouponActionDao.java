package de.hybris.platform.couponservices.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.couponservices.model.RuleBasedAddCouponActionModel;
import java.util.List;

public interface RuleBasedCouponActionDao
{
    List<RuleBasedAddCouponActionModel> findRuleBasedCouponActionByOrder(OrderModel paramOrderModel);
}
