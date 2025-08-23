package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import java.math.BigDecimal;

public interface DeliveryCostEvaluationStrategy
{
    BigDecimal evaluateCost(AbstractOrderModel paramAbstractOrderModel, DeliveryModeModel paramDeliveryModeModel);
}
