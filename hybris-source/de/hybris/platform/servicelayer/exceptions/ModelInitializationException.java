package de.hybris.platform.servicelayer.exceptions;

public class ModelInitializationException extends SystemException
{
    public ModelInitializationException(String message)
    {
        super(message);
    }


    public ModelInitializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
