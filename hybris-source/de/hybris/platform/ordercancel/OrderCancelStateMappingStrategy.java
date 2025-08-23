package de.hybris.platform.ordercancel;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.model.order.OrderModel;

public interface OrderCancelStateMappingStrategy
{
    OrderCancelState getOrderCancelState(OrderModel paramOrderModel);
}
