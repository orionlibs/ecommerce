package de.hybris.platform.ordercancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.OrderModel;
import java.util.List;

public class OrderCancelResponse extends OrderCancelRequest
{
    private final ResponseStatus responseStatus;


    public OrderCancelResponse(OrderModel order, List<OrderCancelEntry> orderCancelEntries)
    {
        super(order, orderCancelEntries);
        this.responseStatus = ResponseStatus.partial;
    }


    public OrderCancelResponse(OrderModel order, List<OrderCancelEntry> orderCancelEntries, ResponseStatus status, String statusMessage)
    {
        super(order, orderCancelEntries, statusMessage);
        this.responseStatus = status;
    }


    public OrderCancelResponse(OrderModel order)
    {
        super(order);
        this.responseStatus = ResponseStatus.full;
    }


    public OrderCancelResponse(OrderModel order, ResponseStatus status, String statusMessage)
    {
        super(order, CancelReason.NA, statusMessage);
        this.responseStatus = status;
    }


    public ResponseStatus getResponseStatus()
    {
        return this.responseStatus;
    }
}
