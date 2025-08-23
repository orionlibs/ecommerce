package de.hybris.platform.ordermanagementfacades.order.data;

import de.hybris.platform.core.enums.OrderStatus;
import java.io.Serializable;
import java.util.List;

public class OrderStatusDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<OrderStatus> statuses;


    public void setStatuses(List<OrderStatus> statuses)
    {
        this.statuses = statuses;
    }


    public List<OrderStatus> getStatuses()
    {
        return this.statuses;
    }
}
