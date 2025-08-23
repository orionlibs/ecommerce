package de.hybris.platform.directpersistence.exception;

public class UpdateRowException extends ModelPersistenceException
{
    public UpdateRowException()
    {
    }


    public UpdateRowException(String message)
    {
        super(message);
    }


    public UpdateRowException(Throwable cause)
    {
        super(cause);
    }


    public UpdateRowException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
