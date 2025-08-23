package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;

public class OrderFraudEvent extends OrderProcessingEvent
{
    public OrderFraudEvent(OrderProcessModel process)
    {
        super(process);
    }
}
