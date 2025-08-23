package de.hybris.platform.ordercancel.exceptions;

import de.hybris.platform.ordercancel.OrderCancelException;

public class OrderCancelRecordsHandlerException extends OrderCancelException
{
    public OrderCancelRecordsHandlerException(String orderCode, String message, Throwable nested)
    {
        super("Order Cancel(" + orderCode + ") :" + message, nested);
    }


    public OrderCancelRecordsHandlerException(String orderCode, String message)
    {
        super(orderCode, "Order Cancel(" + orderCode + ") :" + message);
    }


    public OrderCancelRecordsHandlerException(String orderCode, Throwable nested)
    {
        super("orderCode: " + orderCode, nested);
    }
}
