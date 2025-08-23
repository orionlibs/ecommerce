package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

public class InvalidCouponStateException extends WebserviceException
{
    private static final String TYPE = "InvalidStateError";
    private static final String SUBJECT_TYPE = "state";


    public InvalidCouponStateException(String message)
    {
        super(message);
    }


    public InvalidCouponStateException(String message, String reason)
    {
        super(message, reason);
    }


    public InvalidCouponStateException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public InvalidCouponStateException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public InvalidCouponStateException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public String getType()
    {
        return "InvalidStateError";
    }


    public String getSubjectType()
    {
        return "state";
    }
}
