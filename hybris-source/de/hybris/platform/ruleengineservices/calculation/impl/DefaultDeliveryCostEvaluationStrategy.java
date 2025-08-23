package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ruleengineservices.calculation.DeliveryCostEvaluationStrategy;
import java.math.BigDecimal;

public class DefaultDeliveryCostEvaluationStrategy implements DeliveryCostEvaluationStrategy
{
    public BigDecimal evaluateCost(AbstractOrderModel order, DeliveryModeModel deliveryMode)
    {
        return BigDecimal.ZERO;
    }
}
