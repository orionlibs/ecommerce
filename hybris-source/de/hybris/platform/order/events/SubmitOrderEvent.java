package de.hybris.platform.order.events;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.io.Serializable;

public class SubmitOrderEvent extends AbstractEvent
{
    private OrderModel order;


    public SubmitOrderEvent()
    {
    }


    public SubmitOrderEvent(Serializable source)
    {
        super(source);
        if(source instanceof OrderModel)
        {
            setOrder((OrderModel)source);
        }
    }


    public void setOrder(OrderModel order)
    {
        this.order = order;
    }


    public OrderModel getOrder()
    {
        return this.order;
    }
}
