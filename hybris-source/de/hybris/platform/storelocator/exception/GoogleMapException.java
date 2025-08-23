package de.hybris.platform.storelocator.exception;

public class GoogleMapException extends RuntimeException
{
    public GoogleMapException()
    {
    }


    public GoogleMapException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public GoogleMapException(String message)
    {
        super(message);
    }


    public GoogleMapException(Throwable nested)
    {
        super(nested);
    }
}
