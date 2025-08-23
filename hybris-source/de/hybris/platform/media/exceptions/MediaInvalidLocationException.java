package de.hybris.platform.media.exceptions;

public class MediaInvalidLocationException extends RuntimeException
{
    public MediaInvalidLocationException(String message)
    {
        super(message);
    }


    public MediaInvalidLocationException(Throwable cause)
    {
        super(cause);
    }


    public MediaInvalidLocationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
