package de.hybris.platform.couponservices.services;

public class InvalidCouponStateException extends RuntimeException
{
    public InvalidCouponStateException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public InvalidCouponStateException(String message)
    {
        super(message);
    }
}
