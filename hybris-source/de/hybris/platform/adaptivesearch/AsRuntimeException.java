package de.hybris.platform.adaptivesearch;

public class AsRuntimeException extends RuntimeException
{
    public AsRuntimeException()
    {
    }


    public AsRuntimeException(String message)
    {
        super(message);
    }


    public AsRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AsRuntimeException(Throwable cause)
    {
        super(cause);
    }
}
