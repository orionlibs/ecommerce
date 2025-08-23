package de.hybris.platform.webservicescommons.errors.exceptions;

import de.hybris.platform.webservicescommons.util.YSanitizer;

public abstract class WebserviceException extends RuntimeException
{
    private final String reason;
    private final String subject;


    public WebserviceException(String message)
    {
        super(message);
        this.reason = null;
        this.subject = null;
    }


    public WebserviceException(String message, String reason)
    {
        super(message);
        this.reason = reason;
        this.subject = null;
    }


    public WebserviceException(String message, String reason, Throwable cause)
    {
        super(message, cause);
        this.reason = reason;
        this.subject = null;
    }


    public WebserviceException(String message, String reason, String subject)
    {
        super(message);
        this.reason = reason;
        this.subject = YSanitizer.sanitize(subject);
    }


    public WebserviceException(String message, String reason, String subject, Throwable cause)
    {
        super(message, cause);
        this.reason = reason;
        this.subject = YSanitizer.sanitize(subject);
    }


    public String getReason()
    {
        return this.reason;
    }


    public String getSubject()
    {
        return this.subject;
    }


    public abstract String getType();


    public abstract String getSubjectType();
}
