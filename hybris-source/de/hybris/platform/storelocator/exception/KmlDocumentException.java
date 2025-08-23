package de.hybris.platform.storelocator.exception;

public class KmlDocumentException extends RuntimeException
{
    public KmlDocumentException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public KmlDocumentException(String message)
    {
        super(message);
    }


    public KmlDocumentException(Throwable cause)
    {
        super(cause);
    }
}
