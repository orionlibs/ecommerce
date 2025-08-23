package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;

public class CouponCodeGenerationWsException extends WebserviceException
{
    private static final String TYPE = "CouponCodeGenerationWsException";
    private static final String SUBJECT_TYPE = "state";


    public CouponCodeGenerationWsException(String message)
    {
        super(message);
    }


    public CouponCodeGenerationWsException(String message, String reason)
    {
        super(message, reason);
    }


    public CouponCodeGenerationWsException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CouponCodeGenerationWsException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CouponCodeGenerationWsException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public String getType()
    {
        return "CouponCodeGenerationWsException";
    }


    public String getSubjectType()
    {
        return "state";
    }
}
