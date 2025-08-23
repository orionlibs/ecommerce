package de.hybris.platform.servicelayer.exceptions;

public class ModelNotFoundException extends SystemException
{
    public ModelNotFoundException(String message)
    {
        super(message);
    }


    public ModelNotFoundException(Throwable cause)
    {
        super(cause);
    }


    public ModelNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
