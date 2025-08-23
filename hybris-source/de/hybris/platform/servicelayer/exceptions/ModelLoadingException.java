package de.hybris.platform.servicelayer.exceptions;

public class ModelLoadingException extends SystemException
{
    public ModelLoadingException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public ModelLoadingException(String message)
    {
        super(message);
    }
}
