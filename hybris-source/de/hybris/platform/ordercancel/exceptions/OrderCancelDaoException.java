package de.hybris.platform.ordercancel.exceptions;

public class OrderCancelDaoException extends RuntimeException
{
    private final String orderCode;


    public OrderCancelDaoException(String orderCode, String message, Throwable cause)
    {
        super(message, cause);
        this.orderCode = orderCode;
    }


    public OrderCancelDaoException(String orderCode, String message)
    {
        super(message);
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }
}
