package de.hybris.platform.directpersistence.exception;

public class ConcurrentModificationException extends ModelPersistenceException
{
    public ConcurrentModificationException()
    {
    }


    public ConcurrentModificationException(String message)
    {
        super(message);
    }


    public ConcurrentModificationException(Throwable cause)
    {
        super(cause);
    }


    public ConcurrentModificationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
