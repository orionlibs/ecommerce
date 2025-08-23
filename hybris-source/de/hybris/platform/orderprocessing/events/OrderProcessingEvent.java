package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class OrderProcessingEvent extends AbstractEvent
{
    private final OrderProcessModel process;
    private final OrderStatus orderStatus;


    public OrderProcessingEvent(OrderProcessModel process)
    {
        this.process = process;
        if(process != null)
        {
            OrderModel order = process.getOrder();
            this.orderStatus = (order == null) ? null : order.getStatus();
        }
        else
        {
            this.orderStatus = null;
        }
    }


    public OrderProcessModel getProcess()
    {
        return this.process;
    }


    public OrderStatus getOrderStatus()
    {
        return this.orderStatus;
    }
}
