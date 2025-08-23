package com.hybris.datahub.extensionloading;

public class ExtensionImportException extends Exception
{
    public ExtensionImportException()
    {
    }


    public ExtensionImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public ExtensionImportException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ExtensionImportException(String message)
    {
        super(message);
    }


    public ExtensionImportException(Throwable cause)
    {
        super(cause);
    }
}
