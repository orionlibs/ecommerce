package de.hybris.platform.returns;

public class OrderReturnException extends Exception
{
    private final String orderCode;


    public OrderReturnException(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public OrderReturnException(String orderCode, String message, Throwable nested)
    {
        super("Order Return(" + orderCode + ") :" + message, nested);
        this.orderCode = orderCode;
    }


    public OrderReturnException(String orderCode, String message)
    {
        super("Order Return(" + orderCode + ") :" + message);
        this.orderCode = orderCode;
    }


    public OrderReturnException(String orderCode, Throwable nested)
    {
        super("orderCode: " + orderCode, nested);
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }
}
