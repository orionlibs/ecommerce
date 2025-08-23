package de.hybris.platform.servicelayer.internal.converter;

public class PersistenceObjectNotFoundException extends PersistenceObjectException
{
    public PersistenceObjectNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public PersistenceObjectNotFoundException(String message)
    {
        super(message);
    }
}
