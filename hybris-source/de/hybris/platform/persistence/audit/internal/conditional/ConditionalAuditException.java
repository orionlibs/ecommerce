package de.hybris.platform.persistence.audit.internal.conditional;

public class ConditionalAuditException extends RuntimeException
{
    public ConditionalAuditException()
    {
    }


    public ConditionalAuditException(String msg)
    {
        super(msg);
    }


    public ConditionalAuditException(Throwable cause)
    {
        super(cause);
    }
}
