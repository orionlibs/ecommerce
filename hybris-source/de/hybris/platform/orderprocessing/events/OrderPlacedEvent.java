package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;

public class OrderPlacedEvent extends OrderProcessingEvent
{
    public OrderPlacedEvent(OrderProcessModel process)
    {
        super(process);
    }
}
