package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.OrderModel;
import java.io.Serializable;

public class CommerceOrderResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private OrderModel order;


    public void setOrder(OrderModel order)
    {
        this.order = order;
    }


    public OrderModel getOrder()
    {
        return this.order;
    }
}
