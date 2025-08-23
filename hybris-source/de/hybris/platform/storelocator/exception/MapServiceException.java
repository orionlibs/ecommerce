package de.hybris.platform.storelocator.exception;

public class MapServiceException extends RuntimeException
{
    public MapServiceException()
    {
    }


    public MapServiceException(String message)
    {
        super(message);
    }


    public MapServiceException(Throwable cause)
    {
        super(cause);
    }


    public MapServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
