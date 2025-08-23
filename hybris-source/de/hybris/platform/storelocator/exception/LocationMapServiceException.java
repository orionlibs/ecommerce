package de.hybris.platform.storelocator.exception;

public class LocationMapServiceException extends RuntimeException
{
    public LocationMapServiceException()
    {
    }


    public LocationMapServiceException(String message)
    {
        super(message);
    }


    public LocationMapServiceException(Throwable cause)
    {
        super(cause);
    }


    public LocationMapServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
