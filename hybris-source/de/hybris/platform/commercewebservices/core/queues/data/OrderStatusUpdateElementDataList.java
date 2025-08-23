package de.hybris.platform.commercewebservices.core.queues.data;

import java.io.Serializable;
import java.util.List;

public class OrderStatusUpdateElementDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<OrderStatusUpdateElementData> orderStatusUpdateElements;


    public void setOrderStatusUpdateElements(List<OrderStatusUpdateElementData> orderStatusUpdateElements)
    {
        this.orderStatusUpdateElements = orderStatusUpdateElements;
    }


    public List<OrderStatusUpdateElementData> getOrderStatusUpdateElements()
    {
        return this.orderStatusUpdateElements;
    }
}
