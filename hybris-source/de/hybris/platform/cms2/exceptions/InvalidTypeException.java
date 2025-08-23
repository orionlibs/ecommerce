package de.hybris.platform.cms2.exceptions;

public class InvalidTypeException extends RuntimeException
{
    public InvalidTypeException()
    {
    }


    public InvalidTypeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public InvalidTypeException(String message)
    {
        super(message);
    }


    public InvalidTypeException(Throwable cause)
    {
        super(cause);
    }
}
