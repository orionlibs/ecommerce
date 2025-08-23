package de.hybris.platform.media.exceptions;

public class ExternalStorageServiceException extends RuntimeException
{
    public ExternalStorageServiceException(String message)
    {
        super(message);
    }


    public ExternalStorageServiceException(Throwable cause)
    {
        super(cause);
    }


    public ExternalStorageServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
