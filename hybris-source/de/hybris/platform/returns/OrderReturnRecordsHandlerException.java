package de.hybris.platform.returns;

public class OrderReturnRecordsHandlerException extends OrderReturnException
{
    public OrderReturnRecordsHandlerException(String orderCode, String message, Throwable nested)
    {
        super("Order Return(" + orderCode + ") :" + message, nested);
    }


    public OrderReturnRecordsHandlerException(String orderCode, String message)
    {
        super(orderCode, "Order Return(" + orderCode + ") :" + message);
    }


    public OrderReturnRecordsHandlerException(String orderCode, Throwable nested)
    {
        super("orderCode: " + orderCode, nested);
    }
}
