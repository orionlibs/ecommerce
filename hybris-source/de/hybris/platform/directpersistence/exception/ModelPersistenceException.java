package de.hybris.platform.directpersistence.exception;

public class ModelPersistenceException extends RuntimeException
{
    public ModelPersistenceException()
    {
    }


    public ModelPersistenceException(String message)
    {
        super(message);
    }


    public ModelPersistenceException(Throwable cause)
    {
        super(cause);
    }


    public ModelPersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
