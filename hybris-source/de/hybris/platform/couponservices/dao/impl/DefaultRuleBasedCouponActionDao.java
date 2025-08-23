package de.hybris.platform.couponservices.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.couponservices.dao.RuleBasedCouponActionDao;
import de.hybris.platform.couponservices.model.RuleBasedAddCouponActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultRuleBasedCouponActionDao extends AbstractItemDao implements RuleBasedCouponActionDao
{
    public List<RuleBasedAddCouponActionModel> findRuleBasedCouponActionByOrder(OrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order model", order);
        Objects.requireNonNull(RuleBasedAddCouponActionModel.class);
        Objects.requireNonNull(RuleBasedAddCouponActionModel.class);
        return (List<RuleBasedAddCouponActionModel>)order.getAllPromotionResults().stream().map(PromotionResultModel::getAllPromotionActions).flatMap(Collection::stream).filter(RuleBasedAddCouponActionModel.class::isInstance).map(RuleBasedAddCouponActionModel.class::cast)
                        .collect(Collectors.toList());
    }
}
