package de.hybris.platform.servicelayer.media;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class MediaIOException extends SystemException
{
    public MediaIOException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public MediaIOException(String message)
    {
        super(message);
    }


    public MediaIOException(Throwable cause)
    {
        super(cause);
    }
}
