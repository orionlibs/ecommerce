package de.hybris.platform.servicelayer.exceptions;

public class UnknownIdentifierException extends SystemException
{
    public UnknownIdentifierException(String message)
    {
        super(message);
    }


    public UnknownIdentifierException(Throwable cause)
    {
        super(cause);
    }


    public UnknownIdentifierException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
