package de.hybris.platform.servicelayer.exceptions;

public class ModelValidationException extends SystemException
{
    public ModelValidationException(String message)
    {
        super(message);
    }


    public ModelValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
