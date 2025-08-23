package de.hybris.platform.webservicescommons.errors.exceptions;

public class CodeConflictException extends WebserviceException
{
    public CodeConflictException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public CodeConflictException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CodeConflictException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CodeConflictException(String message, String reason)
    {
        super(message, reason);
    }


    public CodeConflictException(String message)
    {
        super(message);
    }


    public String getType()
    {
        return "CodeConflictError";
    }


    public String getSubjectType()
    {
        return null;
    }
}
