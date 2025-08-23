package de.hybris.deltadetection.exception;

public class ChangesColletorException extends RuntimeException
{
    public ChangesColletorException(String message)
    {
        super(message);
    }


    public ChangesColletorException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ChangesColletorException(Throwable cause)
    {
        super(cause);
    }
}
