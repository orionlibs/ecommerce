package de.hybris.platform.servicelayer.internal.converter;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class PersistenceObjectException extends SystemException
{
    public PersistenceObjectException(String message)
    {
        super(message);
    }


    public PersistenceObjectException(Throwable cause)
    {
        super(cause);
    }


    public PersistenceObjectException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
