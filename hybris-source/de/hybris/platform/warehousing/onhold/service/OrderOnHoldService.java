package de.hybris.platform.warehousing.onhold.service;

import de.hybris.platform.core.model.order.OrderModel;

public interface OrderOnHoldService
{
    void processOrderOnHold(OrderModel paramOrderModel);
}
