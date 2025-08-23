package de.hybris.platform.storelocator.exception;

public class GeoLocatorException extends RuntimeException
{
    public GeoLocatorException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public GeoLocatorException(String message)
    {
        super(message);
    }


    public GeoLocatorException(Throwable cause)
    {
        super(cause);
    }


    public String toString()
    {
        return super.toString() + "\n" + super.toString();
    }
}
