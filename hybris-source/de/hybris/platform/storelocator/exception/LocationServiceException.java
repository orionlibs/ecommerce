package de.hybris.platform.storelocator.exception;

public class LocationServiceException extends RuntimeException
{
    public LocationServiceException()
    {
    }


    public LocationServiceException(String message)
    {
        super(message);
    }


    public LocationServiceException(Throwable cause)
    {
        super(cause);
    }


    public LocationServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
