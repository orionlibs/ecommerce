package de.hybris.platform.webservicescommons.errors.exceptions;

public class AlreadyExistsException extends WebserviceException
{
    private static final String TYPE = "AlreadyExistsError";


    public AlreadyExistsException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public AlreadyExistsException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public AlreadyExistsException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public AlreadyExistsException(String message, String reason)
    {
        super(message, reason);
    }


    public AlreadyExistsException(String message)
    {
        super(message);
    }


    public String getType()
    {
        return "AlreadyExistsError";
    }


    public String getSubjectType()
    {
        return null;
    }
}
