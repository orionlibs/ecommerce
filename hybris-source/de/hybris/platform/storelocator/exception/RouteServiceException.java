package de.hybris.platform.storelocator.exception;

public class RouteServiceException extends RuntimeException
{
    public RouteServiceException()
    {
    }


    public RouteServiceException(String message)
    {
        super(message);
    }


    public RouteServiceException(Throwable cause)
    {
        super(cause);
    }


    public RouteServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
