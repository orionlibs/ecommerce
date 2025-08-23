package de.hybris.platform.fraud.events;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class OrderFraudEmployeeNotificationEvent extends AbstractEvent
{
    private final OrderModel order;


    public OrderFraudEmployeeNotificationEvent(OrderModel order)
    {
        this.order = order;
    }


    public OrderModel getOrder()
    {
        return this.order;
    }
}
