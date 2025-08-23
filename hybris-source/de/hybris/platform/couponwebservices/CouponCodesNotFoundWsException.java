package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

public class CouponCodesNotFoundWsException extends WebserviceException
{
    private static final String TYPE = "CouponCodesNotFoundException";
    private static final String SUBJECT_TYPE = "state";


    public CouponCodesNotFoundWsException(String message)
    {
        super(message);
    }


    public CouponCodesNotFoundWsException(String message, String reason)
    {
        super(message, reason);
    }


    public CouponCodesNotFoundWsException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CouponCodesNotFoundWsException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CouponCodesNotFoundWsException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public String getType()
    {
        return "CouponCodesNotFoundException";
    }


    public String getSubjectType()
    {
        return "state";
    }
}
