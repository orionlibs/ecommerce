package de.hybris.platform.servicelayer.media;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class NoDataAvailableException extends SystemException
{
    public NoDataAvailableException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public NoDataAvailableException(String message)
    {
        super(message);
    }


    public NoDataAvailableException(Throwable cause)
    {
        super(cause);
    }
}
