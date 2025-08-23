package de.hybris.platform.couponservices;

public class CouponServiceException extends RuntimeException
{
    public CouponServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CouponServiceException(String message)
    {
        super(message);
    }
}
