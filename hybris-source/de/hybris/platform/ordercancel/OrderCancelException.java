package de.hybris.platform.ordercancel;

public class OrderCancelException extends Exception
{
    private final String orderCode;


    public OrderCancelException(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public OrderCancelException(String orderCode, String message, Throwable nested)
    {
        super("Order Cancel(" + orderCode + ") :" + message, nested);
        this.orderCode = orderCode;
    }


    public OrderCancelException(String orderCode, String message)
    {
        super("Order Cancel(" + orderCode + ") :" + message);
        this.orderCode = orderCode;
    }


    public OrderCancelException(String orderCode, Throwable nested)
    {
        super("orderCode: " + orderCode, nested);
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }
}
