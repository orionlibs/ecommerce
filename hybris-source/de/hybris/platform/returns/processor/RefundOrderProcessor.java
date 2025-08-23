package de.hybris.platform.returns.processor;

import de.hybris.platform.core.model.order.OrderModel;

public interface RefundOrderProcessor
{
    void process(OrderModel paramOrderModel);
}
