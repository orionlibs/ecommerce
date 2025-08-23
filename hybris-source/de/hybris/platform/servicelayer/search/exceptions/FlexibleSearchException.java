package de.hybris.platform.servicelayer.search.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class FlexibleSearchException extends SystemException
{
    public FlexibleSearchException(String message)
    {
        super(message);
    }


    public FlexibleSearchException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public FlexibleSearchException(Throwable cause)
    {
        super(cause);
    }
}
