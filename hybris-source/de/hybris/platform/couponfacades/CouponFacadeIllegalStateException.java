package de.hybris.platform.couponfacades;

public class CouponFacadeIllegalStateException extends RuntimeException
{
    public CouponFacadeIllegalStateException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CouponFacadeIllegalStateException(String message)
    {
        super(message);
    }
}
