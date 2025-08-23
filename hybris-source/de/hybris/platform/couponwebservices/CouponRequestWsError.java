package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

public class CouponRequestWsError extends WebserviceException
{
    private static final String TYPE = "CouponRequestError";
    private static final String SUBJECT_TYPE = "state";


    public CouponRequestWsError(String message)
    {
        super(message);
    }


    public CouponRequestWsError(String message, String reason)
    {
        super(message, reason);
    }


    public CouponRequestWsError(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CouponRequestWsError(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CouponRequestWsError(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public String getType()
    {
        return "CouponRequestError";
    }


    public String getSubjectType()
    {
        return "state";
    }
}
