package com.hybris.datahub.extensionloading;

public class MalformedExtensionException extends ExtensionImportException
{
    public MalformedExtensionException()
    {
    }


    public MalformedExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public MalformedExtensionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public MalformedExtensionException(String message)
    {
        super(message);
    }


    public MalformedExtensionException(Throwable cause)
    {
        super(cause);
    }
}
