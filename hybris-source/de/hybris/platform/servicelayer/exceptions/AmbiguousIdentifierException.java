package de.hybris.platform.servicelayer.exceptions;

public class AmbiguousIdentifierException extends SystemException
{
    public AmbiguousIdentifierException(String message)
    {
        super(message);
    }


    public AmbiguousIdentifierException(Throwable cause)
    {
        super(cause);
    }


    public AmbiguousIdentifierException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
