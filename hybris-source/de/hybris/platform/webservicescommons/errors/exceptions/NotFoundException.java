package de.hybris.platform.webservicescommons.errors.exceptions;

public class NotFoundException extends WebserviceException
{
    private static final String TYPE = "NotFoundError";


    public NotFoundException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public NotFoundException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public NotFoundException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public NotFoundException(String message, String reason)
    {
        super(message, reason);
    }


    public NotFoundException(String message)
    {
        super(message);
    }


    public String getType()
    {
        return "NotFoundError";
    }


    public String getSubjectType()
    {
        return null;
    }
}
