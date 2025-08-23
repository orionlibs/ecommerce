package de.hybris.platform.impex.exceptions;

public class DecoratorInitException extends ImportExportException
{
    public DecoratorInitException(String message)
    {
        super(message);
    }


    public DecoratorInitException(String message, Throwable exeception)
    {
        super(message, exeception);
    }
}
