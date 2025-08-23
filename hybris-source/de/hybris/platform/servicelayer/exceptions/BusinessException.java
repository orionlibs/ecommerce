package de.hybris.platform.servicelayer.exceptions;

public class BusinessException extends Exception
{
    public BusinessException(String message)
    {
        super(message);
    }


    public BusinessException(Throwable cause)
    {
        super(cause);
    }


    public BusinessException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
