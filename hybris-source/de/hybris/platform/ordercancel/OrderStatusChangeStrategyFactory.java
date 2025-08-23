package de.hybris.platform.ordercancel;

import de.hybris.platform.core.model.order.OrderModel;

public interface OrderStatusChangeStrategyFactory
{
    OrderStatusChangeStrategy getOrderStatusChangeStrategy(OrderModel paramOrderModel);
}
