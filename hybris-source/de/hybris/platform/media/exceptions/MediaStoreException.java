package de.hybris.platform.media.exceptions;

public class MediaStoreException extends RuntimeException
{
    public MediaStoreException(String message)
    {
        super(message);
    }


    public MediaStoreException(Throwable cause)
    {
        super(cause);
    }


    public MediaStoreException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
