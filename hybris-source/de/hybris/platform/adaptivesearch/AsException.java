package de.hybris.platform.adaptivesearch;

public class AsException extends Exception
{
    public AsException()
    {
    }


    public AsException(String message)
    {
        super(message);
    }


    public AsException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AsException(Throwable cause)
    {
        super(cause);
    }
}
