package de.hybris.platform.media.exceptions;

public class MediaNotFoundException extends RuntimeException
{
    public MediaNotFoundException(String message)
    {
        super(message);
    }


    public MediaNotFoundException(Throwable cause)
    {
        super(cause);
    }


    public MediaNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
