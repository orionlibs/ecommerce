package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;

public class CouponNotFoundException extends NotFoundException
{
    private static final String TYPE = "CouponNotFoundError";


    public CouponNotFoundException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public CouponNotFoundException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CouponNotFoundException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CouponNotFoundException(String message, String reason)
    {
        super(message, reason);
    }


    public CouponNotFoundException(String message)
    {
        super(message);
    }


    public String getType()
    {
        return "CouponNotFoundError";
    }
}
