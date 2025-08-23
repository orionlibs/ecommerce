package de.hybris.platform.orderprocessing;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

public interface OrderFulfillmentProcessService
{
    OrderProcessModel startFulfillmentProcessForOrder(OrderModel paramOrderModel);
}
