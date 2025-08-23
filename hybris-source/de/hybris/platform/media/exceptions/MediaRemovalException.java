package de.hybris.platform.media.exceptions;

public class MediaRemovalException extends RuntimeException
{
    public MediaRemovalException(String message)
    {
        super(message);
    }


    public MediaRemovalException(Throwable cause)
    {
        super(cause);
    }


    public MediaRemovalException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
