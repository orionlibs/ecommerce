package de.hybris.platform.impex.exceptions;

public class UnresolvedValueException extends ImportExportException
{
    public UnresolvedValueException(String message, Throwable exThrowable)
    {
        super(message, exThrowable);
    }


    public UnresolvedValueException(String message)
    {
        super(message);
    }
}
