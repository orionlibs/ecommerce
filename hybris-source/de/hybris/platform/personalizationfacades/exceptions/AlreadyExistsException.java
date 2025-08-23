package de.hybris.platform.personalizationfacades.exceptions;

public class AlreadyExistsException extends RuntimeException
{
    public AlreadyExistsException()
    {
    }


    public AlreadyExistsException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AlreadyExistsException(String message)
    {
        super(message);
    }


    public AlreadyExistsException(Throwable cause)
    {
        super(cause);
    }
}
